# b.py
import numpy


# 输入：x0[i]第i个测试点，x[i]样本i，p[i]类i的先验概率，group类别总数，len0维度
# 输出：cla[i]第i个测试点的类别
def classify(x0, x, p, group, len0):
    sigma = getSigma(x, group, len0)
    u = getU(x, group, len0)
    cla = list()

    for i in range(len(x0)):
        cla.append([])

    for i in range(0, len(x0)):
        max = -100000000
        # 根据最大的函数判断值归类
        # 判别函数方程对应《模式分类第二版》P32 （66）~（69）
        for g0 in range(0, group):
            gg = g(x0[i], u[g0], sigma[g0], p[g0])
            if gg > max:
                cla[i] = g0 + 1
                max = gg

    return cla


# 输入：x测试点，u某个类的均值向量，sigma某个类的协方差矩阵，p某个类的先验概率
# 输出：x的判别函数值（方程对应《模式分类第二版》P32 （66）~（69））
def g(x, u, sigma, p):
    Wi = myMult2(-0.5, numpy.linalg.inv(sigma))
    t1 = myMult([x], Wi)
    t2 = myMult(t1, rowToCol(x - u))[0][0]

    wi = myMult(numpy.linalg.inv(sigma), rowToCol(u))
    t3 = myMult([colToRow(wi)], rowToCol(x))[0][0]

    t4 = myMult([u], numpy.linalg.inv(sigma))
    t5 = myMult(t4, rowToCol(u))
    t6 = myMult2(-0.5, t5)[0][0]
    t6 = t6 - 0.5 * numpy.log(numpy.linalg.det(sigma)) + numpy.log(p)

    return t2 + t3 + t6


# ---------------------下列函数已经出现在a.py中----------------------------

def getU(x, group, len0):
    u = list()
    for i in range(group):
        t = list()
        for j in range(len(x)):
            t.append([])
            for z in range(i * len0, i * len0 + len0):
                # print(j, z)
                t[j].append(x[j][z])
        u.append(numpy.mean(t, 0))
    return u


def getSigma(x, group, len0):
    sigma = list()
    u = getU(x, group, len0)

    for i in range(group):
        sigma.append([])

    for i in range(group):
        t = list()
        for j in range(len(x)):
            t.append([])
            for z in range(i * len0, i * len0 + len0):
                t[j].append(x[j][z])
            t[j] = t[j] - u[i]
        sigma[i] = covariance(t)
    return sigma


def covariance(x):
    all = covariance0(x[len(x) - 1])
    for i in range(len(x) - 1):
        add(covariance0(x[i]), all)

    for i in range(len(all)):
        for j in range(len(all[0])):
            all[i][j] /= len(x)

    return all


def covariance0(a):
    aa = []
    for i in range(len(a)):
        aa.append([a[i]])

    aa
    ans = []
    for i in range(len(aa)):
        t = []
        for j in range(len(aa)):
            t.append(aa[i][0] * a[j])
        ans.append(t)
    return ans


# ------------------工具方法------------------


# 利用副作用，把a加到all里
def add(a, all):
    for i in range(len(a)):
        for j in range(len(a[0])):
            all[i][j] += a[i][j]


# 行向量转置为列向量
def rowToCol(x):
    t = list();
    for i in range(len(x)):
        t.append([])

    for i in range(len(x)):
        t[i].append(x[i])
    return t


# 列向量转置为行向量
def colToRow(x):
    t = list()
    for i in range(len(x)):
        t.append(x[i][0])
    return t


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


# 常数*矩阵
def myMult2(n, x):
    ans = list()
    for i in range(len(x)):
        ans.append([])
        for j in range(len(x[0])):
            ans[i].append([])
            ans[i][j] = x[i][j] * n
    return ans


a = [
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

a0 = [
    numpy.array([1, 2, 1]),
    numpy.array([5, 3, 2]),
    numpy.array([0, 0, 0]),
    numpy.array([1, 0, 0]),
]

# p = numpy.array([1 / 3, 1 / 3, 1 / 3])
p = numpy.array([0.8, 0.1, 0.1])

c = classify(a0, a, p, 3, 3)
print(c)
