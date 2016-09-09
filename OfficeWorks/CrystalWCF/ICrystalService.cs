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
    // NOTE: If you change the interface name "ICrystalService" here, you must also update the reference to "ICrystalService" in Web.config.
    [ServiceContract]
    public interface ICrystalService
    {

        #region Properties

        string UserID { [OperationContract] set; }

        string Password { [OperationContract] set; }

        //string ServiceType { [OperationContract] set; }

        DateTime FromDate { [OperationContract] set; }

        int ItemID {[OperationContract] set; }

        DataSet DataSetPR { [OperationContract] set; }

        

        #endregion
        
        #region Crystal Contract

        [OperationContract]       
        DataSet GetInventoryStock();

        [OperationContract]       
        DataSet GetSalesStock();

        [OperationContract]
        bool SetRaiseForPurchaseRequisition();

        [OperationContract]
        DataSet GetPurchaseRequisitionStatus(string PurchaseRequisitionKey);

        [OperationContract]
        bool SendSakthiSMS(string ProjectName, DataSet dsSMS, string UserName);

        [OperationContract]
        string DynamicDataInsertUpdate(string UserName, string SrcConStr, string SrcDet, string DDet);

        [OperationContract]
        DataSet GetConnectionDB(string ConnectionName);
        #endregion

     

    }

    // Use a data contract as illustrated in the sample below to add composite types to service operations.

    #region Crystal DataContract
    [DataContract]
    public class CrystalRights
    {
        bool boolRights = true;
        string userName = "", password = "", servicetype="";

        [DataMember]
        public bool BoolRights
        {
            get { return boolRights; }
            set { boolRights = value; }
        }

        [DataMember]
        public string UserName
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public string Password
        {
            get { return password; }
            set { password = value; }
        }

        [DataMember]
        public string ServiceType
        {
            get { return servicetype; }
            set { servicetype = value; }
        }

        [DataMember]
        public bool IsAuthorisedUser
        {
            get;
            set;
        }

        [DataMember]
        public int ItemID
        {
            get;
            set;
        }

    }
    #endregion

   

}