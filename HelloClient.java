import HelloApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

public class HelloClient {
  static Hello helloImpl;

  public static void main(String args[])
    {
      try{
        // Создание и инициализация ORB
	ORB orb = ORB.init(args, null);

        // Получение корневого контекста именования
        org.omg.CORBA.Object objRef = 
	    orb.resolve_initial_references("NameService");
        // NamingContextExt является частью спецификации Interoperable
        // Naming Service (INS)
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
 
        // Получение доступа к серверу по его имени
        String name = "Hello";
        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));

        System.out.println("Получен доступ к объекту " + helloImpl);
        System.out.println(helloImpl.sayHello());
        helloImpl.shutdown();

	} catch (Exception e) {
          System.out.println("ОШИБКА : " + e) ;
	  e.printStackTrace(System.out);
	  }
    }

}
