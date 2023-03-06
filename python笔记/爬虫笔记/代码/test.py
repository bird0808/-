'''
Descripttion: 
Author: 只会Ctrl CV的菜鸟
version:  
Date: 2023-01-30 21:04:13
LastEditTime: 2023-02-18 23:50:39
'''
import time

import sympy
from sympy import Eq, pprint, simplify, symbols,Float


def get():
    d = sympy.Symbol('d')
    state = 1
    while (state):
        PdivL = Float(input('输入△P/L:'))
        Q0 = Float(input('输入Q0:'))
        sanjiao = Float(input('输入△:'))
    
        v = 8.5675 * 10 ** -6
        p0 = 1.0441
        T0 = 273.15
        T = 289.15
        start = time.time()
        # simplified=sympy.simplify()
        # print('化简后:',simplified)
        eq=sympy.Eq(PdivL**4 * (1/(Q0**2))**4 *(T0/(6.9*10**6*p0*T))**4 * d**20,sanjiao*Q0+192.2*d**2*v)
        # result=sympy.solve(eq,d)
        pprint(eq)
        # print(result)
        print(sympy.solveset(eq,d,domain = sympy.S.Reals))
        print('total times:', time.time() - start)
        state = int(input('继续输入?0/1(输入1继续):'))             

get()