package Cell;


/**
* Cell/StationPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Cell.idl
* 11 ���� 2014 �. 10:57:07 MSK
*/


/* (Y��T �
d
T Y */
public abstract class StationPOA extends org.omg.PortableServer.Servant
 implements Cell.StationOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("register", new java.lang.Integer (0));
    _methods.put ("sendSMS", new java.lang.Integer (1));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {

  /* VW 
  �Y
  e
   d
  d � Z
  W
  d W TubeCallback */
       case 0:  // Cell/Station/register
       {
         Cell.TubeCallback objRef = Cell.TubeCallbackHelper.read (in);
         String phoneNum = in.read_string ();
         String to = in.read_string ();
         int $result = (int)0;
         $result = this.register (objRef, phoneNum, to);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }


  /* .Zd 
  
  ��Y� message 
   Y
  	� fromNum  Y
  	� toNum */
       case 1:  // Cell/Station/sendSMS
       {
         String fromNum = in.read_string ();
         String toNum = in.read_string ();
         String message = in.read_string ();
         int $result = (int)0;
         $result = this.sendSMS (fromNum, toNum, message);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:Cell/Station:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Station _this() 
  {
    return StationHelper.narrow(
    super._this_object());
  }

  public Station _this(org.omg.CORBA.ORB orb) 
  {
    return StationHelper.narrow(
    super._this_object(orb));
  }


} // class StationPOA
