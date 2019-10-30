# a.py
import numpy


# 输入：x0[i]第i个测试点，x[i]第i个样本数据，group类总数，len0维度（描述一个类的元素总数）
# 输出：dis[i][j]表示第i个测试点和第j个类的均值向量的距离
def mahalanobis(x0, x, group, len0):
    dis = list()
    u = getU(x, group, len0)

    for i in range(len(x0)):
        xx = numpy.array(x0[i])
        sigma = getSigma(x, group, len0)
        dis.append([])
        for j in range(len(u)):
            u[j] = numpy.array(u[j])

            # sqrt( (x-μ)'Σ^(-1)(x-μ) ) 计算Mahalanobis距离
            t = [xx - u[j]]
            t1 = myMult(t, numpy.linalg.inv(sigma[j]))
            t2 = myMult(t1, rowToCol(xx - u[j]))

            dis[i].append(t2[0][0])

    return dis


# 输入：（同上）
# 输出：u[i]是类i的均值向量
def getU(x, group, len0):
    u = list()
    for i in range(group):
        t = list()
        for j in range(len(x)):
            t.append([])
            for z in range(i * len0, i * len0 + len0):
                t[j].append(x[j][z])
        u.append(numpy.mean(t, 0))
    return u


# 输入：（同上）
# 输出：返回值sigma[i]表示第i类的协方差矩阵
def getSigma(x, group, len0):
    sigma = list()
    u = getU(x, group, len0)

    for i in range(group):
        sigma.append([])

    for i in range(group):
        # t对所有样本数据，只保留类i的列
        t = list()
        for j in range(len(x)):
            t.append([])
            for z in range(i * len0, i * len0 + len0):
                t[j].append(x[j][z])
            t[j] = t[j] - u[i]
        sigma[i] = covariance(t)
    return sigma


# 输入：x对所有样本数据，只保留某个类的列
# 输出：某个类的协方差矩阵
def covariance(x):
    all = covariance0(x[len(x) - 1])
    for i in range(len(x) - 1):
        add(covariance0(x[i]), all)

    for i in range(len(all)):
        for j in range(len(all[0])):
            all[i][j] /= len(x)

    return all

# 输入：a一个单独的样本数据，并且只保留某个类的列
# 输出：a的协方差矩阵
def covariance0(a):
    aa = []
    for i in range(len(a)):
        aa.append([a[i]])

    ans = []
    for i in range(len(aa)):
        t = []
        for j in range(len(aa)):
            t.append(aa[i][0] * a[j])
        ans.append(t)
    return ans


# ---------------------工具方法------------------------------


# 行向量转置为列向量
def rowToCol(x):
    t = list();
    for i in range(len(x)):
        t.append([])

    for i in range(len(x)):
        t[i].append(x[i])
    return t


# 利用副作用，把矩阵a加到矩阵all里
def add(a, all):
    for i in range(len(a)):
        for j in range(len(a[0])):
            all[i][j] += a[i][j]


# 矩阵乘法
def myMult(a, b):
    ans = list()
    for i in range(len(a)):
        ans.append([])

    for i in range(len(a)):
        for j in range(len(b[0])):
            ii = 0
            jj = 0
            ans[i].append(0)
            while ii < len(a[0]):
                ans[i][j] = ans[i][j] + (a[i][ii] * b[jj][j])
                ii = ii + 1
                jj = jj + 1
    return ans


# 样本数据
x = [
    numpy.array([-5.01, -8.12, -3.68, -0.91, -0.18, -0.05, 5.35, 2.26, 8.13]),
    numpy.array([-5.43, -3.48, -3.54, 1.30, -2.06, -3.53, 5.12, 3.22, -2.66]),
    numpy.array([1.08, -5.52, 1.66, -7.75, -4.54, -0.95, -1.34, -5.31, -9.87]),
    numpy.array([0.86, -3.78, -4.11, -5.47, 0.50, 3.92, 4.48, 3.42, 5.19]),
    numpy.array([-2.67, 0.63, 7.39, 6.14, 5.72, -4.85, 7.11, 2.39, 9.12]),
    numpy.array([4.94, 3.29, 2.08, 3.60, 1.26, 4.36, 7.17, 4.33, -0.98]),
    numpy.array([-2.51, 2.09, -2.59, 5.37, -4.63, -3.65, 5.75, 3.97, 6.65]),
    numpy.array([-2.25, -2.13, -6.94, 7.18, 1.46, -6.66, 0.77, 0.27, 2.41]),
    numpy.array([5.56, 2.86, -2.26, -7.39, 1.17, 6.30, 0.90, -0.43, -8.71]),
    numpy.array([1.03, -3.33, 4.33, -7.50, -6.32, -0.31, 3.52, -0.36, 6.43]),
]

# 测试点
x0 = [
    numpy.array([1, 2, 1]),
    numpy.array([5, 3, 2]),
    numpy.array([0, 0, 0]),
    numpy.array([1, 0, 0]),
]

ans = mahalanobis(x0, x, 3, 3)

for i in range(len(ans)):
    print(ans[i])
