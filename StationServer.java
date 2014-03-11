import Cell.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;

class Connection{
  TubeCallback objRef;
  String phoneNum;
  String to;
  public Connection(TubeCallback obj, String num, String tonum){
    objRef = obj;
    phoneNum = num;
    to = tonum;
  }
  public TubeCallback getTubeCallback(){return objRef;}
  public String getTo(){ return to;}
};
// Класс, реализующий IDL-интерфейс базовой станции
class StationServant extends StationPOA {
  // Вместо представленных ниже двух переменных здесь
  // должен быть список пар "номер - объектная ссылка"
  StationServant(){
    // tubeRef = new TubeCallback[2];
    // tubeNum = new String[2];
    listNum = new HashMap<String, Connection>();
  }
  // TubeCallback tubeRef[];
  // String tubeNum[];
  // String tubeN[];
  HashMap<String, Connection> listNum;
  // Метод регистрации трубки в базовой станции
  public int register (TubeCallback objRef, String phoneNum, String to) {
     // tubeRef[tubeRef.length] = objRef;
     // tubeNum[tubeNum.length] = phoneNum;
     listNum.put(phoneNum, new Connection(objRef, phoneNum, to));
     System.out.println("Station (INFO): Registered new tube "+ phoneNum);
     if (listNum.containsKey(to)) {
      listNum.get(phoneNum).getTubeCallback().setRefDest(listNum.get(to).getTubeCallback());
      listNum.get(to).getTubeCallback().setRefDest(listNum.get(phoneNum).getTubeCallback());
    }
     return (1);
     };

  // Метод пересылки сообщения от трубки к трубке
  public int sendSMS (String fromNum, String toNum, String message) {
    System.out.println("Station (INFO): Tube "+fromNum+" sends message to "+toNum);
    if (!listNum.containsKey(toNum)) {
      listNum.get(fromNum).getTubeCallback().sendSMS(fromNum, "Station (ERROR): No such user");
      return (1);
    }
    // listNum.get(fromNum).getTubeCallback().setRefDest(listNum.get(toNum).getTubeCallback());
    // listNum.get(toNum).getTubeCallback().setRefDest(listNum.get(fromNum).getTubeCallback());
    // Здесь должен быть поиск объектной ссылки по номеру toNum
    // listNum.get(fromNum).getTubeCallback().sendSMS(fromNum, message);
    // listNum.get(toNum).getTubeCallback().getSMS(fromNum, message);
    return (1);
    };
  };



// Класс, реализующий сервер базовой станции
public class StationServer {

  public static void main(String args[]) {
    try{
      // Создание и инициализация ORB
      ORB orb = ORB.init(args, null);

      // Получение ссылки и активирование POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // Создание серванта для CORBA-объекта "базовая станция" 
      StationServant servant = new StationServant();

      // Получение объектной ссылки на сервант
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(servant);
      Station sref = StationHelper.narrow(ref);
          
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // Связывание объектной ссылки с именем
      String name = "BaseStation";
      NameComponent path[] = ncRef.to_name( name );
      ncRef.rebind(path, sref);

      System.out.println("Station (INFO): Server is ready and waiting for clients ...");

      // Ожидание обращений от клиентов (трубок)
      orb.run();
      } 
     catch (Exception e) {
        System.err.println("Station (ERROR): " + e);
        e.printStackTrace(System.out);
        System.out.println("Tube (ERROR): orbd is not started");
      };
    };
  };