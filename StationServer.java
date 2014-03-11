import Cell.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import java.util.StringTokenizer;

// Класс, реализующий IDL-интерфейс базовой станции
class StationServant extends StationPOA {
  
  /*
  TubeCallback tubeRef;
  String tubeNum;
  */
  Map<String, TubeCallback> tubesInCell = new HashMap<String, TubeCallback>();
  Set<String> snouden = new HashSet<String>();

  // Метод регистрации трубки в базовой станции
  public int register ( TubeCallback objRef, String phoneNum ) {
     /*
     tubeRef = objRef;
     tubeNum = phoneNum;
     */
     tubesInCell.put( phoneNum, objRef );
     System.out.println( "BaseStation (INFO): Tube " + phoneNum + " is registered" );
     System.out.println( "BaseStation (INFO): Number of registered tubes: " + tubesInCell.size() );     
     return (1);
  };

  // Метод пересылки сообщения от трубки к трубке
  public int sendSMS ( String fromNum, String toNum, String message ) {
    System.out.println( "BaseStation (INFO): Tube " + fromNum + " sends SMS to " + toNum);
    // Здесь должен быть поиск объектной ссылки по номеру toNum
    StringTokenizer st = new StringTokenizer( message );

  //!!!!
    snouden.add("USA");
    snouden.add("ukraine");
    snouden.add("tadjik");
    snouden.add("putin");
    snouden.add("bomb");
  //!!!!

    while ( st.hasMoreTokens() )
    {
      String buf = st.nextToken();
      if ( snouden.contains( buf ) )
        System.out.println( "BaseStation (WARNING): bad word \'" + buf + "\' was used" );
    }

    tubesInCell.get( toNum ).sendSMS( fromNum, message );
    
    // tubeRef.sendSMS(fromNum, message);

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

      System.out.println("BaseStation (INFO): Server is ready ...");

      // Ожидание обращений от клиентов (трубок)
      orb.run();
    } 
    catch (Exception e) {
        System.err.println("BaseStation (ERROR): " + e);
        e.printStackTrace(System.out);
    };
  };

};
