import numpy
import wave
from scipy.linalg import toeplitz


def get_formants(file_path):
    counter = 0
    spf = wave.open(file_path, 'r')
    result = []
    x = spf.readframes(-1)
    x = numpy.fromstring(x, "Int16")
    window = frames(x, 128)
    for i in window:
        n = len(i)
        w = numpy.hamming(n)
        x1 = i * w
        a, e = lpc(x1, 13)
        v = lpcc(a, e)
        result.insert(counter,v)
        counter += 1
    return result


def frames(x, frameSize):
        frames = []
        start = 0
        end = frameSize
        while start < len(x):
            frame = x[start:end]
            if len(frame) < frameSize:
                diff = frameSize - len(frame)
                frame = numpy.append(frame, [0] * diff)
            frames.append(frame)
            start = start + frameSize
            end = end + frameSize
        return frames


def lpcc(seq, err_term, order=None):
    if order is None:
        order = len(seq) - 1
    lpcc_coeffs = [numpy.log(err_term), -seq[0]]
    for n in range(2, order + 1):
        upbound = (order + 1 if n > order else n)
        lpcc_coef = -sum(i * lpcc_coeffs[i] * seq[n - i - 1]
                         for i in range(1, upbound)) * 1. / upbound
        lpcc_coef -= seq[n - 1] if n <= len(seq) else 0
        lpcc_coeffs.append(lpcc_coef)
    return lpcc_coeffs


def autocorr(self, order=None):
        if order is None:
            order = len(self) - 1
        return [sum(self[n] * self[n + tau] for n in range(len(self) - tau))
                for tau in range(order + 1)]


def lpc(seq, order=None):
    # In this lpc method we use the slow( if the order is >50) autocorrelation approach.
    acseq = numpy.array(autocorr(seq, order))
    # Using pseudoinverse to obtain a stable estiamte of the toeplitz matrix
    a_coef = numpy.dot(numpy.linalg.pinv(toeplitz(acseq[:-1].T)), -acseq[1:].T)
    # Squared prediction error, defined as e[n] = a[n] + \sum_k=1^order (a_k *
    # s_{n-k})
    err_term = acseq[0] + sum(a * c for a, c in zip(acseq[1:], a_coef))
    return a_coef.tolist(), numpy.sqrt(err_term)
