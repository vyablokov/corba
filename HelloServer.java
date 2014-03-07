// ----- Импорт всех необходимых пакетов -----

// Пакет, сгенерированный компилятором idlj
import HelloApp.*;

// Пакет, необходимый для использования службы имен
import org.omg.CosNaming.*;

// Пакет, содержащий специальные исключения, генерируемые службой имен
import org.omg.CosNaming.NamingContextPackage.*;

// Пакет, содержащий классы, необходимые любому приложению CORBA
import org.omg.CORBA.*;

// Пакеты, содержащие классы, реализующие модель наследования переносимых серверов
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

// Пакет, содержащий классы, необходимые для инициализации ORB
import java.util.Properties;

// ----- Реализация класса-серванта -----

// Сервант HelloImpl является подклассом класса HelloPOA и наследует
// всю функциональность CORBA, сгенерированную для него компилятором
class HelloImpl extends HelloPOA {
  private ORB orb;	// Необходима для хранения текущего ORB (используется в методе shutdown)

  public void setORB(ORB orb_val) {
    orb = orb_val; 
    }
    
  public String sayHello() {
    return "\nHello, world !!\n";
    }
    
  public void shutdown() {
  // Использует метод org.omg.CORBA.ORB.shutdown(boolean) для завершения ORB,
  //false означает, что завершение должно быть немедленным
        orb.shutdown(false);
    }
  }

// ----- Реализация класса-сервера -----

public class HelloServer {
// Сервер создает один или несколько объектов-серванов.
// Сервант наследует интерфейсу, сгенерированному компилятором idlj,
// и реально выполняет все операции этого интерфейса.

    public static void main(String args[]) {
    try{
      // Создаем и инициализируем экземпляр ORB
      ORB orb = ORB.init(args, null);

      // Получаем доступ к Root POA и активируем POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // Создаем объект сервант и регистрируем в нем объект ORB
      HelloImpl helloImpl = new HelloImpl();
      helloImpl.setORB(orb); 

      // Получаем доступ к объекту серванта
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
      Hello href = HelloHelper.narrow(ref);
	  
      // Получаем корневой контекст именования
      org.omg.CORBA.Object objRef =
          orb.resolve_initial_references("NameService");
      // NamingContextExt является частью спецификации Interoperable
      // Naming Service (INS)
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // Связывание идентификатора "Hello" и объекта серванта
      String name = "Hello";
      NameComponent path[] = ncRef.to_name( name );
      ncRef.rebind(path, href);

      System.out.println("HelloServer готов и ждет обращений ...");

      // Ожидание обращений клиентов
      orb.run();
      } 	
    catch (Exception e) {
      System.err.println("ОШИБКА: " + e);	// Выводим сообщение об ошибке
      e.printStackTrace(System.out);	// Выводим содержимое стека вызовов
      };
	  
      System.out.println("HelloServer работу завершил ...");
	
  }
}