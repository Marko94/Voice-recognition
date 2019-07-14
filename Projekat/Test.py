import pyaudio
import wave
import numpy
import warnings
from sqlite3 import Time, time

from sys import byteorder
from array import array
from struct import pack
from pydub import AudioSegment
from builtins import print
from LPC import get_formants
from DTW import dtw
from sklearn.metrics.pairwise import euclidean_distances
AudioSegment.converter = "C:\\ffmpeg\\bin\\ffmpeg.exe"

THRESHOLD = 700
CHUNK_SIZE = 1024
FORMAT = pyaudio.paInt16
RATE = 44100


def is_silent(snd_data):
    return max(snd_data) < THRESHOLD


def normalize(snd_data):
    MAXIMUM = 16384
    times = float(MAXIMUM)/max(abs(i) for i in snd_data)

    r = array('h')
    for i in snd_data:
        r.append(int(i*times))
    return r


def trim(snd_data):
    def _trim(snd_data):
        snd_started = False
        r = array('h')

        for i in snd_data:
            if not snd_started and abs(i)>THRESHOLD:
                snd_started = True
                r.append(i)

            elif snd_started:
                r.append(i)
        return r

    # Trim to the left
    snd_data = _trim(snd_data)

    # Trim to the right
    snd_data.reverse()
    snd_data = _trim(snd_data)
    snd_data.reverse()
    return snd_data


def add_silence(snd_data, seconds):
    r = array('h', [0 for i in range(int(seconds*RATE))])
    r.extend(snd_data)
    r.extend([0 for i in range(int(seconds*RATE))])
    return r


def record():
    start = time.time()

    while time.time() - start < 2:{}

    print("Recording started")
    p = pyaudio.PyAudio()
    stream = p.open(format=FORMAT, channels=1, rate=RATE,
        input=True, output=True,
        frames_per_buffer=CHUNK_SIZE)

    num_silent = 0
    snd_started = False

    r = array('h')

    while 1:
        # little endian, signed short
        snd_data = array('h', stream.read(CHUNK_SIZE))
        if byteorder == 'big':
            snd_data.byteswap()
        r.extend(snd_data)

        silent = is_silent(snd_data)

        if silent and snd_started:
            num_silent += 1
        elif not silent and not snd_started:
            snd_started = True

        if snd_started and num_silent > 180:
            break

    sample_width = p.get_sample_size(FORMAT)
    stream.stop_stream()
    stream.close()
    p.terminate()

    r = normalize(r)
    r = trim(r)
    # r = add_silence(r, 0.5)
    return sample_width, r


def split(file):
    afile = wave.open(file, "rb")
    n = afile.getnframes() // CHUNK_SIZE
    global num_silent1
    num_silent1 = 0
    counter2 = 0
    p = pyaudio.PyAudio()
    sample_width = p.get_sample_size(FORMAT)
    nfile = wave.open("search" + str(counter2) + ".wav", "w")
    opened = 1
    nfile.setnchannels(1)
    nfile.setframerate(RATE)
    nfile.setsampwidth(sample_width)
    for i in range(0, n):
        info = afile.readframes(CHUNK_SIZE)
        info = numpy.fromstring(info, "Int16")
        if is_silent(info):
            if opened == 1:
                nfile.writeframes(info)
            num_silent1 += 1
            # print(num_silent1)
        else:
            if opened == 0:
                counter2 += 1
                # print("treba da otvori novi fajl")
                nfile = wave.open("Search" + str(counter2) + ".wav", "w")
                opened = 1
                nfile.setnchannels(1)
                nfile.setframerate(RATE)
                nfile.setsampwidth(sample_width)
            num_silent1 = 0
            nfile.writeframes(info)
        if num_silent1 > 4 :
            opened = 0
            nfile.close()

    nfile.close()
    return counter2+1


def fragment(data, word):
    data_len = len(data)
    word_len = len(word)

    seg_size = data_len // word_len
    res = []
    temp = numpy.array([float(0)] * 13)
    cnt = seg_size

    for i in data:
        cnt -= 1
        temp += numpy.array(i)

        if cnt == 0:
            temp /= numpy.array(float(seg_size))
            res.append(list(temp))
            temp = numpy.array([float(0)] * 13)
            cnt = seg_size

    if cnt != seg_size:
        temp /= numpy.array(float(seg_size))
        res.append(list(temp))

    return res


def record_to_file(path):
    sample_width, data = record()
    data = pack('<' + ('h'*len(data)), *data)
    wf = wave.open(path, 'wb')
    wf.setnchannels(1)
    wf.setsampwidth(sample_width)
    wf.setframerate(RATE)
    wf.writeframes(data)
    wf.close()


if __name__ == '__main__':
    while 1:
        option = input("\nRecord/Search/Multisearch/Exit\n")
        if option == "Record":
            word = input("Write the word you want to record\n")
            user = input("Write the name of the person speaking\n")
            print("Please speak a word into the microphone\n")
            record_to_file(word + ".wav")
            print("done - result written to " + word + ".wav\n")
            sample = get_formants(word + ".wav")
            with open("Database.txt", "a") as txt_file:
                txt_file.write(user)
                txt_file.write("\n")
                txt_file.write(word)
                txt_file.write("\n")
                numpy.savetxt(word + ".cff", sample)
                txt_file.write(word + ".cff")
                txt_file.write("\n")
        elif option == "Search":
            record_to_file("Search.wav")
            # k = split("Search.wav")
            print("Recording finished, search in progress")
            # print(k)
            word1 = get_formants("Search.wav")
            counter = 0
            speaker = ""
            recorded_word = ""
            found = 0
            with open("Database.txt", "r") as f:
                for line in f:
                    if counter % 3 == 0:
                        speaker = line
                    elif counter % 3 == 1:
                        recorded_word = line
                    else:
                        word2 = numpy.loadtxt(line[:-1])
                        warnings.simplefilter("ignore")
                        dist = dtw(word1, word2, euclidean_distances)
                        if dist < 0.485:
                            found = 1
                            print("Prepoznao rec ", recorded_word, " koju je izgovorio ", speaker,
                                  " a razdaljina iznosi: ", dist)
                    counter += 1
            if found == 0:
                print("Nije prepoznao rec")
        elif option == "Multisearch":
            record_to_file("Search.wav")
            k = split("Search.wav")
            print("Recording finished, search in progress")
            print(k)
            word1 = get_formants("Search.wav")
            counter = 0
            speaker = ""
            recorded_word = ""
            found = 0
            for q in range(0, k):
                word1 = get_formants("search" + str(q) + ".wav")
                counter = 0
                speaker = ""
                recorded_word = ""
                found = 0
                print("Poredi se rec: ", q)
                with open("Database.txt", "r") as f:
                    for line in f:
                        if counter % 3 == 0:
                            speaker = line
                        elif counter % 3 == 1:
                            recorded_word = line
                        else:
                            word2 = numpy.loadtxt(line[:-1])
                            warnings.simplefilter("ignore")
                            dist = dtw(word1, word2, euclidean_distances)
                            if dist < 0.65:
                                found = 1
                                print("Prepoznao rec ", recorded_word, " koju je izgovorio ", speaker, " a razdaljina iznosi: ", dist)

                        counter += 1
                if found == 0:
                    print("Nije prepoznao rec")
        else:
            break

