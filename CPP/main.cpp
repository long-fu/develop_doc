#include <stdio.h>  
#include <iostream>  
class Parent  
{  
public:  
    void F()  
    {  
        printf("Parent.F()\n");  
    }  
    virtual void G()  
    {  
        printf("Parent.G()\n");  
    }  
    int Add(int x, int y)  
    {  
        return x + y;  
    }  
    //重载(overload)Add函数  
    float Add(float x, float y)  
    {  
        return x + y;  
    }  
};  
  
class ChildOne:Parent  
{  
    //重写(overwrite)父类函数  
    void F()  
    {  
        // this->F();
        
        printf("ChildOne.F()\n");   
    }  
    //覆写(override)父类虚函数,主要实现多态  
    void G()  
    {  
        // Parent::G(); 强制调用基类
        printf("ChildOne.G()\n");  
    }  
};  
  
  
int main()  
{  
    ChildOne childOne;// = new ChildOne();  
    // childOne.
    Parent* p = (Parent*)&childOne;  
    //调用Parent.F()  
    p->F();  
    //实现多态
      
    p->G();
    // p->Add
    Parent* p2 = new Parent();  
    //重载(overload)  
    printf("%d/n",p2->Add(1, 2));  
    printf("%f/n",p2->Add(3.4f, 4.5f));  
    delete p2;  
    // system("PAUSE");  
    return 0;  
}