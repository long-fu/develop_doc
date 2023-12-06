
## CPP语言实现面向对象三大特性：封装、继承、多态


-----

[c++中重载、重写、覆盖的区别](https://www.cnblogs.com/huolong-blog/p/7588422.html)

[虚函数](https://blog.csdn.net/weixin_52967653/article/details/124729546)

[C++ 虚函数表剖析](https://zhuanlan.zhihu.com/p/75172640)

[构造函数可以是虚函数吗？](https://blog.csdn.net/m0_46657980/article/details/117535442)

[new-delete与malloc-free](https://blog.csdn.net/weixin_46065476/article/details/124609924)

[C++强制类型转换](https://www.cnblogs.com/qinguoyi/p/10303912.html)


-----------------


[C++](https://cloud.tencent.com/developer/user/2542479)

## OPP

[【C++】多态 ⑤ ( 重载 | 重写 | 重定义 )](https://cloud.tencent.com/developer/article/2353302)

### 重载 Overloading

>  函数重载 必须是在 同一个类中进行的 ;
> 子类无法重载父类的函数 , 如果 子类 函数名和参数列表 与 父类相同 , 那是 重写 Overriding ;
> 这些 函数或操作符 在 参数类型 或 数量上 有所不同 ; 也就是 参数列表 不同 ;
> 返回值 并不作为 函数重载 的 判定标准 ;

编译的时候确定调用那个函数

### 重写 Overriding

子类 重写 父类 的 函数 , 子类中 重写父类的 函数 , 必须与 父类的函数 的 函数原型 是相同的 ;

使用 virtual 关键字修饰 父类 和 子类 的函数 , 才叫重写 ; 如果 没有使用 virtual 关键字 , 是 " 重定义 " ;

" 多态 " 是 在 函数运行期间 , 根据 实际对象 的类型 , 决定调用哪个函数 ; " 重载 " 在编译阶段决定调用哪个函数 ;

### 重定义 Redefinition

如果 子类 重写 父类 的函数 , 分两种情况 :

虚函数重写 : 使用了 virtual 关键字修饰 被重写的函数 , 是 重写 操作 , 会产生 多态 效果 ;

非虚函数重写 : 没有使用 virtual 关键字 , 就是 重定义 ;

子类 中 重定义 父类的函数 , 会导致 父类 的 同名函数被覆盖 , 如果想要调用父类的函数 , 需要使用 `父类类名::被重定义的函数()` 方式调用 ;


[【C++】多态 ⑥ ( 函数重定义涉及的问题 - 子类覆盖父类函数名 )](https://cloud.tencent.com/developer/article/2353303)


```cpp
#include "iostream"
using namespace std;

// 父类
class Parent {
public:
	void fun(int a, int b, int c)
	{
		cout << "执行 父类 void fun(int a, int b, int c) 函数" << endl;
	}

	void fun(int a, int b)
	{
		cout << "执行 父类 void fun(int a, int b) 函数" << endl;
	}

	void fun(int a)
	{
		cout << "执行 父类 void fun(int a) 函数" << endl;
	}
};

// 子类
class Child : public Parent {
public:
	void fun(int a, int b)
	{
		cout << "执行 子类 void fun(int a, int b) 函数" << endl;
	}

	void fun(int a)
	{
		cout << "执行 子类 void fun(int a) 函数" << endl;
	}
};


int main() {

	Child c;
    // 错误 执行失败
	c.fun(1, 2, 3);

	// 控制台暂停 , 按任意键继续向后执行
	system("pause");

	return 0;
}
```

3、错误原因分析 - 函数重定义问题 : 子类覆盖父类函数名
错误原因分析 : 函数重定义 带来的问题 , 子类覆盖父类函数名 ;

函数重定义的函数名称覆盖问题 : C++ 编译器 发现 Child c 对象要调用 void fun(int a, int b, int c) 函数 , 子类中已经存在 fun 函数了 , 子类 会 覆盖 父类的函数名 , C++ 编译器只会在 子类查找 该函数 , 不会去父类 查找 ;

子类查找函数 : C++ 编译器 在 子类中找到了 void fun(int a, int b) 和 void fun(int a) 两个函数 , 没有找到 3 个参数的函数 , 此时 C++ 编译器会报错 :

```cpp
#include "iostream"
using namespace std;

// 父类
class Parent {
public:
	void fun(int a, int b, int c)
	{
		cout << "执行 父类 void fun(int a, int b, int c) 函数" << endl;
	}

	void fun(int a, int b)
	{
		cout << "执行 父类 void fun(int a, int b) 函数" << endl;
	}

	void fun(int a)
	{
		cout << "执行 父类 void fun(int a) 函数" << endl;
	}
};

// 子类
class Child : public Parent {
public:
	void fun(int a, int b)
	{
		cout << "执行 子类 void fun(int a, int b) 函数" << endl;
	}

	void fun(int a)
	{
		cout << "执行 子类 void fun(int a) 函数" << endl;
	}
};


int main() {

	Child c;
	c.Parent::fun(1, 2, 3);

	// 控制台暂停 , 按任意键继续向后执行
	system("pause");

	return 0;
}
```