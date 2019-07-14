from numpy import zeros, inf


def dtw(x, y, dist):
    r, c = len(x), len(y)
    D0 = zeros((r + 1, c + 1))
    D0[0, 1:] = inf
    D0[1:, 0] = inf
    D1 = D0[1:, 1:]
    for i in range(r):
        for j in range(c):
            D1[i, j] = dist(x[i], y[j])
    for i in range(r):
        for j in range(c):
            D1[i, j] += min(D0[i, j], D0[i, j+1], D0[i+1, j])
    return D1[-1, -1] / sum(D1.shape)

