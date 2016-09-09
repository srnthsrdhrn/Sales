using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.Data;
using System.ServiceModel.Web;

namespace CrystalWCF
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IAndroid" in both code and config file together.
    [ServiceContract]
    public interface IAndroid
    {
        #region Android Contract

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetMachine")]
        List<Machine_mst> GetMachine();

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetUser")]
        List<User_mst> GetUser();

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetSchedule")]
        List<Schedule_Mst> GetSchedule();

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetProblem")]
        List<Key_Value> GetProblem();

        [OperationContract]
        ////[XmlSerializerFormat(Style = OperationFormatStyle.Document, Use = OperationFormatUse.Literal)]

        [WebInvoke(BodyStyle = WebMessageBodyStyle.Wrapped, RequestFormat = WebMessageFormat.Json,
            Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "Save_Achivement/{UserId}/{MachineId}/{ScheduleId}/{ForDate}/{LValue}/{RValue}/{FromTime}/{ToTime}/{ProblemId}")]
        int Save_Achivement(string UserId, string MachineId, string ScheduleId, string ForDate, string LValue, string RValue, string FromTime, string ToTime, string ProblemId);

        [OperationContract]
        ////[XmlSerializerFormat(Style = OperationFormatStyle.Document, Use = OperationFormatUse.Literal)]

        [WebInvoke(BodyStyle = WebMessageBodyStyle.Wrapped, RequestFormat = WebMessageFormat.Json,
            Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "Save_Problem/{UserId}/{MachineId}/{ScheduleId}/{ForDate}/{LValue}/{RValue}/{FromTime}/{ToTime}/{ProblemId}")]
        int Save_Problem(string UserId, string MachineId, string ScheduleId, string ForDate, string LValue, string RValue, string FromTime, string ToTime, string ProblemId);

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetScheduleReport")]
        List<Chart_Report> GetScheduleReport();

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetLineReport")]
        List<Chart_Report> GetLineReport();

        [OperationContract]
        [WebInvoke(Method = "GET",
                  ResponseFormat = WebMessageFormat.Json,
                  BodyStyle = WebMessageBodyStyle.Wrapped,
                  UriTemplate = "Verify_BarCodeDetails/{SupplierCode}/{Quantity}/{Part}/{DeliveryDocumentNo}/{SerialNo}")]
        string Verify_BarCodeDetails(string SupplierCode, string Quantity, string Part, string DeliveryDocumentNo, string SerialNo);       

        #endregion
    }

    #region Android DataContract
    [DataContract]
    public class User_mst
    {
        private string m_userid;
        private string m_password;
        private string m_username;
        private string m_phone;

        [DataMember]
        public string UserId
        {
            get { return m_userid; }
            set { m_userid = value; }
        }
        [DataMember]
        public string Password
        {
            get { return m_password; }
            set { m_password = value; }
        }
        [DataMember]
        public string UserName
        {
            get { return m_username; }
            set { m_username = value; }
        }
        [DataMember]
        public string Phone
        {
            get { return m_phone; }
            set { m_phone = value; }
        }
    }

    [DataContract]
    public class Machine_mst
    {
        private int m_id;
        private int m_parentid;
        private string m_description;
        private string m_Align;

        [DataMember]
        public string Description
        {
            get { return m_description; }
            set { m_description = value; }
        }
        [DataMember]
        public int Id
        {
            get { return m_id; }
            set { m_id = value; }
        }
        [DataMember]
        public int ParentId
        {
            get { return m_parentid; }
            set { m_parentid = value; }
        }
        [DataMember]
        public string Align
        {
            get { return m_Align; }
            set { m_Align = value; }
        }
    }

    [DataContract]
    public class Schedule_Mst
    {
        private int m_id;
        private string m_fromtime;
        private string m_totime;

        [DataMember]
        public string FromTime
        {
            get { return m_fromtime; }
            set { m_fromtime = value; }
        }
        [DataMember]
        public int Id
        {
            get { return m_id; }
            set { m_id = value; }
        }
        [DataMember]
        public string ToTime
        {
            get { return m_totime; }
            set { m_totime = value; }
        }
    }

    [DataContract]
    public class Key_Value
    {
        private int m_id;
        private string m_Description;

        [DataMember]
        public string Description
        {
            get { return m_Description; }
            set { m_Description = value; }
        }
        [DataMember]
        public int Id
        {
            get { return m_id; }
            set { m_id = value; }
        }
    }

    [DataContract]
    public class Chart_Report
    {
        private decimal m_Target;
        private decimal m_Achieved;
        private string m_Description;

        [DataMember]
        public string Description
        {
            get { return m_Description; }
            set { m_Description = value; }
        }
        [DataMember]
        public decimal Target
        {
            get { return m_Target; }
            set { m_Target = value; }
        }
        [DataMember]
        public decimal Achieved
        {
            get { return m_Achieved; }
            set { m_Achieved = value; }
        }
    }

    [DataContract]
    public class Verify_BarCodeDetails
    { 
        private string m_SupplierCode,m_Part,m_DeliveryDocumentNo,m_SerialNo,m_Quantity;

        [DataMember]
        public string SupplierCode
        {
            get { return m_SupplierCode; }
            set { m_SupplierCode = value; } 
        }

        [DataMember]
        public string Part
        {
            get { return m_Part; }
            set { m_Part = value; }
        }

        [DataMember]
        public string DeliveryDocumentNo
        {
            get { return m_DeliveryDocumentNo; }
            set { m_DeliveryDocumentNo = value; }
        }

        [DataMember]
        public string SerialNo
        {
            get { return m_SerialNo; }
            set { m_SerialNo = value; }
        }

        [DataMember]
        public string Quantity
        {
            get { return m_Quantity; }
            set { m_Quantity = value; }
        }
    }

   

    #endregion
}
