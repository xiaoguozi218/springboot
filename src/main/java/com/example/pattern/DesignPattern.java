package com.example.pattern;

/**
 * @author  xiaoguozi
 * @create  2018/6/9 上午11:47
 * @desc    设计模式 总结
 *  问题： 谈谈你知道的设计模式？ 请手动实现单例模式，Spring等框架中使用了那些模式？
 *    典型回答： 大致按照模式的应用目标分类，设计模式可以分为：
 *             1、 创建性模式：是对象创建过程的各种问题和解决方案的总结，包括各种工厂模式（Factory、Abstract Factory）、单例模式（Singleton）、构建器模式（Builder）、原型模式（Prototype）。
 *             2、 结构性模式：是针对软件设计结构的总结，关注于类、对象继承、组合方式的实践经验。常见的结构性模式，包括桥接模式（Bridge）、适配器模式（Adapter）、装饰者模式（Decorator）、代理模式（Proxy）、组合模式（Composite）、外观模式（Facade）、享元模式（Flyweight）等
 *             3、 行为性模式：是从类或对象之间交互、职责划分等角度总结的模式。比较常见的行为型模式有策略模式（Strategy）、解析器模式（Interpreter）、观察者模式（Observer）、迭代器模式（Iterator）、模版方法模式（Template Method）、访问者模式（Visitor）、命令模式（Command）
 *
 *  例1、 刚介绍过IO框架，我们知道 InputStream 是一个 抽象类，标准类库中提供了FileInputStream、ByteArrayInputStream等各种不同的子类，分别从不同角度对InputStream进行了 功能扩展，这是典型的 装饰器模式 应用案例。
 *
 *  识别 装饰器模式，可以通过识别 类设计特征 来进行判断，也就是其类构造函数以 相同的 抽象类或者接口为输入参数。  因为装饰器模式本质上是包装同类型实例，我们对目标对象的调用，往往会通过包装类覆盖过的方法，迂回调用被包装的实例，这就可以很自然地实现 增加额外逻辑 的目的，也就是所谓的"装饰"。
 *      例如，BufferedInputStream经过包装，为输入流过程增加缓存，类似这种装饰器还可以多次嵌套，不断地增加不同层次的功能。
 *
 *  例2、创建型模式尤其是工厂模式，在我们的代码中随处可见。 比如，JDK最新版本中HTTP/2 Client API，下面这个创建HttpRequest的过程，就是典型的构建器模式（Builder）
 *       使用构建器模式，可以比较优雅地解决构建复杂对象的麻烦，这里的"复杂"是指类似需要输入的参数组合较多，如果用构造函数，我们往往需要为每一种可能的输入参数组合实现相应的构造函数，一系列复杂的构造函数会让代码阅读性和可维护性变得很差。
 *       上面的分析也进一步反映了创建型模式的初衷，即 将对象创建过程单独抽象出来，从结构上把对象 使用逻辑 和 创建逻辑 相互独立，隐藏对象实例的细节，进而为使用者实现了更加规范、统一的逻辑。
 *
 *
 *
 *
 *  前面说了不少代码实践，下面一起来简要看看主流开源框架，如Spring 等如何在API设计中使用设计模式。 你至少要有个大体的印象，比如：
 *      1、BeanFactory和ApplicationContext应用了 工厂模式 。
 *      2、在Bean 的创建中，Spring也为不同scope定义的对象，提供了单例和原型等模式实现。
 *      3、在介绍的AOP领域则是使用了 代理模式、 装饰器模式、适配器模式等。
 *      4、各种事件监听器，是 观察者模式 的典型应用。
 *      5、类似JdbcTemplate 等则是应用了模版模式。
 *
 *  谈谈在实际开发中 门面模式 的应用？
 *      门面模式 为子系统中一组接口提供一个统一访问的接口，降低了客户端与子系统之间的耦合，简化了系统复杂度。
 *          缺点：违反了开闭原则。
 *          适用于为一系列复杂的子系统提供一个友好的简单的入口，将子系统与客户端解耦。 公司基础paas平台用到了门面模式，具体是定义一个ServiceFacade,然后通过继承众多xxService，对外提供服务。
 *
 *
 **/
public class DesignPattern {







}