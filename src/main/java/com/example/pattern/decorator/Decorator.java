package com.example.pattern.decorator;

/**
 * Created by MintQ on 2018/5/16.
 * 装饰器模式，顾名思义，拘束将某个类重新装扮一下，使得它比原来更“漂亮”， 或者在功能上更强大，这就是装饰器模式所要达到的目的。但是作为原来的这个类的使用者，还不应该感受到装饰前和装饰后又什么不同，否则就破坏了原有类的结构了。
 *
 * 装饰器模式有如下结构：

 　　• Component：抽象组件，定义了一组抽象的接口，规定这个被装饰类有哪些功能。

 　　• ConcreteComponent： 实现这个抽象组件的所有功能。

 　　• Decorator：装饰器角色， 它持有一个Component 对象实例的引用。

 　　• ConcreteDecorator：具体的装饰器实现者，负责实现装饰器角色定义的功能。

 *
 * 装饰器和适配器模式，他们看似都是起到包装一个类或者对象的作用，但是使用他们的目的很不一样。

 　　适配器模式的意思是要将一个接口转变成另一个接口，它的目的是通过改变接口来达到重复使用的目的；
 而装饰器模式不是要改变被装饰对象的接口，而是恰恰要保持原有的接口，但是增强原有对象的功能，或者改变原有对象的处理方法而提升性能。
 所以这两个模式设计的目的是不同的。
 *
 *
 *
 *
 *  3、定义一个装饰器类
 */
public class Decorator implements Component {

    protected Component component;

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public void operation() {
        if (component != null) {
            component.operation();
        }
    }
}
