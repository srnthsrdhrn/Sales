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
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IIceberg" in both code and config file together.
    [ServiceContract]
    public interface IIceberg
    {
        #region Iceberg OperationContract

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetIDs/{UserId}/{RequestType}/{MachineId}/{ItemId}/{TranUID}")]        
        List<GetIceBerg_Det> GetIDs(string UserId,string RequestType,string MachineId,string ItemId,string TranUID);

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "ValidateUser/{UserId}/{Password}")]
        List<GetIceBerg_Det> ValidateUser(string UserId,string Password);

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat =WebMessageFormat.Json,
            BodyStyle=WebMessageBodyStyle.Wrapped,
            UriTemplate ="FTQSave/{UserId}/{ProcessId}/{PalletId}/{ItemId}/{JulianCode}/{RejectionType}/{ReasonId}/{Quantity}/{DispMsg}/{ErrMsg}")]
        List<GetIceBerg_Det> FTQSave(string UserId, string ProcessId, string PalletId, string ItemId, string JulianCode, string RejectionType, string ReasonId, string Quantity, string DispMsg, string ErrMsg);

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "SuspectReviewSave/{UserId}/{FTQTranUID}/{ActionType}/{ProblemType}/{Remarks}/{Defects}/{DispMsg}/{ErrMsg}")]
        List<GetIceBerg_Det> SuspectReviewSave(string UserId, string FTQTranUID, string ActionType, string ProblemType, string Remarks, string Defects, string DispMsg, string ErrMsg);

        #endregion
    }

    #region Iceberg DataContract
    [DataContract]
    public class GetIceBerg_Det
    {

        private string m_UserId;
        private string m_UserName;
        private string m_Password;

        private int m_ItemId;
        private string m_ItemCode;
        private string m_ItemName;

        private int m_MachineId;
        private string m_MachineName;     
        private int m_PalletId;
        private string m_Pallet;

        private int m_ProcessId;
        private string m_Process;
        private int m_OrderBy;

        private int m_ReasonId;
        private string  m_Reason;

        private int m_PunchId;
        private string m_PunchNo;

        private int m_RejectionTypeId;
        private string m_RejectionType;

        private int m_ReviewId;
        private string m_ReviewAction;

        private int m_ProblemId;
        private string m_Problem;

        private int m_JulianId;
        private string m_JulianCode;
        
        private string m_ErrorMsg;
        private string m_DisplayMsg;

        //---------------------------USER DETAILS----------------------------------------------------------

        [DataMember]
        public string UserId
        {
            get { return m_UserId; }
            set { m_UserId = value; }
        }

        [DataMember]
        public string UserName
        {
            get { return m_UserName; }
            set { m_UserName = value; }
        }

        [DataMember]
        public string Password
        {
            get { return m_Password; }
            set { m_Password = value; }
        }
        
        //---------------------------ITEM DETAILS----------------------------------------------------------

        [DataMember]
        public int ItemId
        {
            get { return m_ItemId; }
            set { m_ItemId = value; }
        }

        [DataMember]
        public string ItemCode
        {
            get { return m_ItemCode; }
            set { m_ItemCode = value; }
        }

        [DataMember]
        public string ItemName
        {
            get { return m_ItemName; }
            set { m_ItemName = value; }
        }

        //---------------------------REASON DETAILS----------------------------------------------------------

        [DataMember]
        public int ReasonId
        {
            get { return m_ReasonId; }
            set { m_ReasonId = value; }
        }

        [DataMember]
        public string Reason
        {
            get { return m_Reason; }
            set { m_Reason = value; }
        }           

        //---------------------------PROCESS DETAILS----------------------------------------------------------

        [DataMember]
        public int ProcessId
        {
            get { return m_ProcessId; }
            set { m_ProcessId = value; }
        }

        [DataMember]
        public string Process
        {
            get { return m_Process; }
            set { m_Process = value; }
        }

        [DataMember]
        public int OrderBy
        {
            get { return m_OrderBy; }
            set { m_OrderBy = value; }
        }

        //---------------------------MACHNE DETAILS------------------------------------------------------------

        [DataMember]
        public int MachineId
        {
            get { return m_MachineId; }
            set { m_MachineId = value; }
        }

        [DataMember]
        public string MachineName
        {
            get { return m_MachineName; }
            set { m_MachineName = value; }
        }


        [DataMember]
        public int PalletId
        {
            get { return m_PalletId; }
            set { m_PalletId = value; }
        }

        [DataMember]
        public string Pallet
        {
            get { return m_Pallet; }
            set { m_Pallet = value; }
        }

        //-------------------------------ERROR DETAILS------------------------------------------------------------

        [DataMember]
        public string ErrorMsg
        {
            get { return m_ErrorMsg; }
            set { m_ErrorMsg = value; }
        }

        [DataMember]
        public string DisplayMsg
        {
            get { return m_DisplayMsg; }
            set { m_DisplayMsg = value; }
        }

        //-------------------------------PUNCH DETAILS------------------------------------------------------------

        [DataMember]
        public int PunchId
        {
            get { return m_PunchId; }
            set { m_PunchId = value; }
        }

        [DataMember]
        public string PunchNo
        {
            get { return m_PunchNo; }
            set { m_PunchNo = value; }        
        }

        //------------------------------REJECTION TYPE----------------------------------------------------------

        [DataMember]
        public int RejectionId
        {
            get { return m_RejectionTypeId; }
            set { m_RejectionTypeId = value; }
        }

        [DataMember]
        public string RejectionType
        {
            get { return m_RejectionType; }
            set { m_RejectionType = value; }
        }

        //-----------------------------REVIEW ACTION------------------------------------------------------------

        [DataMember]
        public int ReviewId
        {
            get { return m_ReviewId; }
            set { m_ReviewId = value; }
        }

        [DataMember]
        public string ReviewAction
        {
            get { return m_ReviewAction; }
            set { m_ReviewAction = value; }
        }

        //-----------------------------REVIEW PROBLEM TYPE-----------------------------------------------------

        [DataMember]
        public int ProblemId
        {
            get { return m_ProblemId; }
            set { m_ProblemId = value; }
        }

        [DataMember]
        public string Problem
        {
            get { return m_Problem; }
            set { m_Problem = value; }
        }

        //------------------------------JULIAN CODE-----------------------------------------------------------

        [DataMember]
        public int JulianId
        {
            get { return m_JulianId; }
            set { m_JulianId = value; } 
        }

        [DataMember]
        public string JulianCode
        {
            get { return m_JulianCode; }
            set { m_JulianCode = value; }
        }
    }

    #endregion
}
