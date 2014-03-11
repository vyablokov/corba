import Cell.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.io.*;

// Класс вызова телефонной трубки
class TubeCallbackServant extends TubeCallbackPOA {
 String myNum;	// Номер трубки
 TubeCallback refdest;
 // Конструктор класса
 TubeCallbackServant (String num) {
   myNum = num;
   refdest = null;
   };

 // Метод обработки принятого сообщения
 public int sendSMS(String fromNum, String message) {
    System.out.println("Tube (INFO): sent message from "+fromNum+": "+message);
    return (0);
    };
 public int getSMS(String fromNum, String message) {
    System.out.println("Tube (INFO): Recieved message from "+fromNum+": "+message);
    return (0);
    };
 // Метод, возвращающий номер трубки
 public String getNum() {
    return (myNum);
    };
 public void setRefDest(TubeCallback ref){
  refdest = ref;
 };
 public TubeCallback getTubeCallback(){
  return refdest;
 };
  };
 
// Класс, используемый для создания потока управления
class ORBThread extends Thread {
  ORB myOrb;

  // Конструктор класса
  ORBThread(ORB orb) {
    myOrb = orb;
    };

   // Метод запуска потока
   public void run() {
     myOrb.run();
     };
  };
 
// Класс, имитирующий телефонную трубку
public class Tube {

  public static void main(String args[]) {
    try {
      String myNum = args[4];	// Номер трубки
      // for (int i=0; i<5; i++) {
      //   System.out.println("My Num: "+ args[i]);
      // }
      System.out.println("Tube (INFO): My Num: "+ myNum);
      // Создание и инициализация ORB
      ORB orb = ORB.init(args, null);

      //Создание серванта для IDL-интерфейса TubeCallback
      POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootPOA.the_POAManager().activate();
      TubeCallbackServant listener  = new TubeCallbackServant(myNum);
      rootPOA.activate_object(listener);
      // Получение ссылки на сервант
      TubeCallback ref = TubeCallbackHelper.narrow(rootPOA.servant_to_reference(listener));
      
      // Получение контекста именования
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
      NamingContext ncRef = NamingContextHelper.narrow(objRef);
      
      // Преобразование имени базовой станции в объектную ссылку
      NameComponent nc = new NameComponent("BaseStation", "");
      NameComponent path[] = {nc};
      Station stationRef = StationHelper.narrow(ncRef.resolve(path));

      // Регистрация трубки в базовой станции
      BufferedReader inpt  = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Tube (DIALOG): Enter the number to connect: ");
      String to = inpt.readLine();
      stationRef .register(ref, myNum, to);
      System.out.println("Tube (INFO): Tube was registered by base station");

      // Запуск ORB в отдельном потоке управления
      // для прослушивания вызовов трубки
      ORBThread orbThr = new ORBThread(orb);
      orbThr.start();

      // Бесконечный цикл чтения строк с клавиатуры и отсылки их
      // базовой станции
      
      String msg;
      TubeCallback dest;
      System.out.println("Tube (DIALOG): Enter your message: ");
      while (true) { 
        // station.checkref(nyNum, to);
        msg = inpt.readLine();
        dest = listener.getTubeCallback();
        if(dest!=null){
          break;
        }
        
        stationRef .sendSMS(myNum, to, msg);
        // Обратите внимание: номер получателя 7890 в описанной ранее
        // реализации базовой станции роли не играет
        }
        while(true){
          dest.getSMS(myNum, msg);
          msg = inpt.readLine();
        }
      } catch (Exception e) {
	     e.printStackTrace();
       System.out.println("Tube (ERROR): Base station not found");
      };


    };

  };