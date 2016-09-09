using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;

namespace CrystalWCF
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Iceberg" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Iceberg.svc or Iceberg.svc.cs at the Solution Explorer and start debugging.
    public class IcebergService : IIceberg
    {
        string IConnection = System.Configuration.ConfigurationManager.ConnectionStrings["IcebergCon"].ConnectionString;

        public List<GetIceBerg_Det> GetIDs(string UserId, string ReturnType,string MachineId,string ItemId,string TranUID)
        {

            DataSet DsIds = new DataSet();
            string ErrorMsg;
            SqlParameter[] param = new SqlParameter[6];

            param[0] = new SqlParameter("@UserId", SqlDbType.VarChar);
            param[0].Value = UserId;          

            param[1] = new SqlParameter("@ReturnType", SqlDbType.VarChar);
            param[1].Value = ReturnType;

            param[2] = new SqlParameter("@ErrorMsg", SqlDbType.VarChar,1000);
            param[2].Direction = ParameterDirection.Output;

            param[3] = new SqlParameter("@MachineId", SqlDbType.Int);
            if (Convert.ToString(MachineId).Trim().ToUpper() == "NULL")
            { param[3].Value = DBNull.Value; }
            else
            { param[3].Value = Convert.ToInt32(MachineId); }

            param[4] = new SqlParameter("@ItemId", SqlDbType.Int);
            if (Convert.ToString(ItemId).Trim().ToUpper() == "NULL")
            { param[4].Value = DBNull.Value; }
            else
            { param[4].Value = Convert.ToInt32(ItemId); }

            param[5] = new SqlParameter("@TranUID", SqlDbType.Int);
            if (Convert.ToString(TranUID).Trim().ToUpper() == "NULL")
            { param[5].Value = DBNull.Value; }
            else
            { param[5].Value = Convert.ToInt32(Convert.ToString(TranUID).Trim() == "" ? "0" : Convert.ToString(TranUID).Trim()); }

            DsIds = DataAccessHelper.DataAccess.ExecuteDataSet(IConnection, "Usp_Android_IceBerg_GetIds", CommandType.StoredProcedure, param);

            ErrorMsg = Convert.ToString(param[2].Value);
            List<GetIceBerg_Det> LstIDs = new List<GetIceBerg_Det>();

            if (ErrorMsg != "")
            {
                GetIceBerg_Det Lst = new GetIceBerg_Det();
                Lst.ErrorMsg = ErrorMsg;
                return LstIDs;
            }

            if(ReturnType == "ProcessId")
            {                
                foreach (DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();                   
                    Lst.ProcessId = Convert.ToInt32(Convert.ToString(Drow["ProcessId"]).Trim() == "" ? "0" : Convert.ToString(Drow["ProcessId"]).Trim());
                    Lst.Process = Convert.ToString(Drow["Process"]).Trim();
                    Lst.OrderBy = Convert.ToInt32(Convert.ToString(Drow["OrderBy"]).Trim() == "" ? "0" : Convert.ToString(Drow["OrderBy"]).Trim());
                    LstIDs.Add(Lst);
                }               
            }

            if (ReturnType == "MachineId")
            {
                foreach (DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();                   
                    Lst.MachineId = Convert.ToInt32(Convert.ToString(Drow["MachineId"]).Trim() == "" ? "0" : Convert.ToString(Drow["MachineId"]).Trim());
                    Lst.MachineName = Convert.ToString(Drow["MachineName"]).Trim() == "" ? "" : Convert.ToString(Drow["MachineName"]).Trim();
                    Lst.PalletId = Convert.ToInt32(Convert.ToString(Drow["PalletId"]).Trim() == "" ? "0" : Convert.ToString(Drow["PalletId"]).Trim());
                    Lst.Pallet = Convert.ToString(Drow["Pallet"]).Trim() == "" ? "" : Convert.ToString(Drow["Pallet"]).Trim();

                    LstIDs.Add(Lst);
                }            
            }

            if (ReturnType == "ItemId")
            {
                foreach (DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();
                    Lst.ItemId = Convert.ToInt32(Convert.ToString(Drow["ItemId"]).Trim() == "" ? "0" : Convert.ToString(Drow["ItemId"]).Trim());
                    Lst.ItemCode = Convert.ToString(Drow["ItemCode"]).Trim() == "" ? "" : Convert.ToString(Drow["ItemCode"]).Trim();
                    Lst.ItemName = Convert.ToString(Drow["ItemName"]).Trim() == "" ? "" : Convert.ToString(Drow["ItemName"]).Trim();
                    LstIDs.Add(Lst);
                }
            }

            if (ReturnType == "ReasonId")
            {
                foreach (DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();
                    Lst.ReasonId = Convert.ToInt32(Convert.ToString(Drow["ReasonId"]).Trim() == "" ? "0" : Convert.ToString(Drow["ReasonId"]).Trim());
                    Lst.Reason = Convert.ToString(Drow["Reason"]).Trim() == "" ? "" : Convert.ToString(Drow["Reason"]).Trim();                    
                    LstIDs.Add(Lst);
                }
            }

            if (ReturnType == "PunchNo")
            { 
                foreach(DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();
                    Lst.PunchId = Convert.ToInt32(Convert.ToString(Drow["PunchId"]).Trim() == "" ? "0" : Convert.ToString(Drow["PunchId"]).Trim());
                    Lst.PunchNo = Convert.ToString(Drow["PunchNo"]).Trim();
                    LstIDs.Add(Lst);
                }
            }

            if (ReturnType == "RejectionType")
            {
                foreach (DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();
                    Lst.RejectionId = Convert.ToInt32(Convert.ToString(Drow["RejectionTypeId"]).Trim() == "" ? "0" : Convert.ToString(Drow["RejectionTypeId"]).Trim());
                    Lst.RejectionType = Convert.ToString(Drow["RejectionType"]).Trim();
                    LstIDs.Add(Lst);
                }
            }

            if (ReturnType == "ReviewAction")
            {
                foreach (DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();
                    Lst.ReviewId = Convert.ToInt32(Convert.ToString(Drow["UID"]).Trim() == "" ? "0" : Convert.ToString(Drow["UID"]).Trim());
                    Lst.ReviewAction = Convert.ToString(Drow["Action"]).Trim();
                    LstIDs.Add(Lst);
                }
            }

            if (ReturnType == "ReviewProblemType")
            {
                foreach (DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();
                    Lst.ProblemId = Convert.ToInt32(Convert.ToString(Drow["UID"]).Trim() == "" ? "0" : Convert.ToString(Drow["UID"]).Trim());
                    Lst.Problem = Convert.ToString(Drow["ProblemType"]).Trim();
                    LstIDs.Add(Lst);
                }
            }

            if (ReturnType == "Julian")
            { 
                foreach(DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();
                    Lst.JulianId = Convert.ToInt32(Convert.ToString(Drow["UID"]).Trim() == "" ? "0" : Convert.ToString(Drow["UID"]).Trim());
                    Lst.JulianCode = Convert.ToString(Drow["JulianCode"]).Trim();
                    LstIDs.Add(Lst);
                }
            }

            if (ReturnType == "JulianDet")
            {
                foreach (DataRow Drow in DsIds.Tables[0].Rows)
                {
                    GetIceBerg_Det Lst = new GetIceBerg_Det();
                    Lst.Process = Convert.ToString(Drow["Process"]).Trim();
                    Lst.MachineName = Convert.ToString(Drow["MachineName"]).Trim();
                    Lst.Pallet = Convert.ToString(Drow["PalletNo"]).Trim();
                    Lst.ItemName = Convert.ToString(Drow["PartName"]).Trim();
                    Lst.ItemId = Convert.ToInt32(Convert.ToString(Drow["ItemId"]).Trim() == "" ? "0" : Convert.ToString(Drow["ItemId"]).Trim());
                    Lst.Reason = Convert.ToString(Drow["Reason"]).Trim() == "" ? "" : Convert.ToString(Drow["Reason"]).Trim();
                    LstIDs.Add(Lst);
                }

                foreach (DataRow Drow in DsIds.Tables[1].Rows)
                {
                    GetIceBerg_Det Lst1 = new GetIceBerg_Det();
                    Lst1.ReasonId = Convert.ToInt32(Convert.ToString(Drow["UID"]).Trim() == "" ? "0" : Convert.ToString(Drow["UID"]).Trim());
                    Lst1.Reason = Convert.ToString(Drow["Reason"]).Trim() == "" ? "" : Convert.ToString(Drow["Reason"]).Trim();     
                    LstIDs.Add(Lst1);
                }
            }
        
            return LstIDs;

        }

        public List<GetIceBerg_Det> ValidateUser(string UserId,string Password)
        {
            List<GetIceBerg_Det> LstIDs = new List<GetIceBerg_Det>();
            SqlParameter[] param = new SqlParameter[4];

            param[0] = new SqlParameter("@UserId", SqlDbType.VarChar);
            param[0].Value = UserId;
            param[1] = new SqlParameter("@Password", SqlDbType.VarChar,100);
            param[1].Value = Password;
            param[1].Direction = ParameterDirection.InputOutput;
            param[2] = new SqlParameter("@DisplayMsg", SqlDbType.VarChar,1000);
            param[2].Direction = ParameterDirection.Output;
            param[3] = new SqlParameter("@ErrorMsg", SqlDbType.VarChar,1000);
            param[3].Direction = ParameterDirection.Output;
            DataSet DSUser = DataAccessHelper.DataAccess.ExecuteDataSet(IConnection, "Usp_MKT_ValidateUser", CommandType.StoredProcedure, param);
            string DisplayMsg = Convert.ToString(param[2].Value);
            string ErrorMsg = Convert.ToString(param[3].Value);
            string Pwd = Convert.ToString(param[1].Value);

            string ValidateMsg =(ErrorMsg == "" ? DisplayMsg : ErrorMsg);

            if (ValidateMsg != "")
            {
                GetIceBerg_Det Lst = new GetIceBerg_Det();
                Lst.ErrorMsg = ValidateMsg;
                Lst.Password = Pwd;
                Lst.DisplayMsg = DisplayMsg;
                LstIDs.Add(Lst);
            }

            //if (DSUser.Tables.Count > 1)
            //{               
            //    foreach (DataRow DIrow in DSUser.Tables[0].Rows)
            //    {
            //        GetIceBerg_Det Lst = new GetIceBerg_Det();
            //        Lst.ItemId = Convert.ToInt32(Convert.ToString(DIrow["ItemId"]).Trim() == "" ? "0" : Convert.ToString(DIrow["ItemId"]).Trim());
            //        Lst.ItemCode = Convert.ToString(DIrow["ItemCode"]).Trim() == "" ? "" : Convert.ToString(DIrow["ItemCode"]).Trim();
            //        Lst.ItemName = Convert.ToString(DIrow["ItemName"]).Trim() == "" ? "" : Convert.ToString(DIrow["ItemName"]).Trim();
            //        LstIDs.Add(Lst);
            //    }
            //    foreach (DataRow Drow in DSUser.Tables[1].Rows)
            //    {
            //        GetIceBerg_Det Lst = new GetIceBerg_Det();
            //        Lst.MachineId = Convert.ToInt32(Convert.ToString(Drow["MachineId"]).Trim() == "" ? "0" : Convert.ToString(Drow["MachineId"]).Trim());
            //        Lst.MachineName = Convert.ToString(Drow["MachineName"]).Trim() == "" ? "" : Convert.ToString(Drow["MachineName"]).Trim();
            //        Lst.PalletId = Convert.ToInt32(Convert.ToString(Drow["PalletId"]).Trim() == "" ? "0" : Convert.ToString(Drow["PalletId"]).Trim());
            //        Lst.Pallet = Convert.ToString(Drow["Pallet"]).Trim() == "" ? "" : Convert.ToString(Drow["Pallet"]).Trim();
            //        LstIDs.Add(Lst);
            //    }             
            //}

            return LstIDs;
        }

        public List<GetIceBerg_Det> SuspectReviewSave(string UserId, string FTQTranUID, string ActionType, string ProblemType, string Remarks, string Defects, string DispMsg, string ErrMsg)
        {
            List<GetIceBerg_Det> LstIDs = new List<GetIceBerg_Det>();
            SqlParameter[] ParamSave = new SqlParameter[8];
            ParamSave[0] = new SqlParameter("@UserId", SqlDbType.VarChar, 10);
            ParamSave[0].Value = Convert.ToString(UserId).Trim();

            ParamSave[1] = new SqlParameter("@FTQTranUID", SqlDbType.Decimal);
            ParamSave[1].Value = Convert.ToInt32(FTQTranUID);

            ParamSave[2] = new SqlParameter("@ActionTypeGcd", SqlDbType.Decimal);
            ParamSave[2].Value = Convert.ToInt32(ActionType);

            ParamSave[3] = new SqlParameter("@ProblemTypeGCD", SqlDbType.Decimal);
            ParamSave[3].Value = Convert.ToInt32(ProblemType);

            ParamSave[4] = new SqlParameter("@Remarks", SqlDbType.VarChar, 500);
            ParamSave[4].Value = Remarks.Trim();

            ParamSave[5] = new SqlParameter("@Message", SqlDbType.VarChar, 300);
            ParamSave[5].Direction = ParameterDirection.Output;

            ParamSave[6] = new SqlParameter("@ErrMessage", SqlDbType.VarChar, 300);
            ParamSave[6].Direction = ParameterDirection.Output;

            ParamSave[7] = new SqlParameter("@QCMUIDs", SqlDbType.VarChar, 8000);

            if (Defects.Trim() == "NULL")
            {
                Defects = ",";
            }

            ParamSave[7].Value = Defects;

            int rowaff = DataAccessHelper.DataAccess.ExecuteNonQuery(IConnection, "USP_QC_Mob_Suspect_Review_Insert",CommandType.StoredProcedure ,ParamSave);

            if (!string.IsNullOrEmpty(Convert.ToString(ParamSave[6].Value).Trim()))
            {
                ErrMsg = Convert.ToString(ParamSave[6].Value).Trim();
            }

            DispMsg = Convert.ToString(ParamSave[5].Value);            

            GetIceBerg_Det Lst = new GetIceBerg_Det();
            Lst.ErrorMsg = ErrMsg;
            Lst.DisplayMsg = DispMsg;
            LstIDs.Add(Lst);
            return LstIDs;
        }

        public List<GetIceBerg_Det> FTQSave(string UserId, string ProcessId, string PalletId, string ItemId, string JulianCode, string RejectionType, string ReasonId, string Quantity, string DispMsg, string ErrMsg)
        {
            List<GetIceBerg_Det> LstIDs = new List<GetIceBerg_Det>();

            SqlParameter[] ParamSave = new SqlParameter[9];
            ParamSave[0] = new SqlParameter("@UserId", SqlDbType.VarChar, 10);
            if (Convert.ToString(UserId).Trim().ToUpper() == "NULL")
            { ParamSave[0].Value = DBNull.Value; }
            else
            { ParamSave[0].Value = Convert.ToString(UserId).Trim(); }

            ParamSave[1] = new SqlParameter("@ProcessUid", SqlDbType.Decimal);
            if (Convert.ToString(ProcessId).Trim().ToUpper() == "NULL")
            { ParamSave[1].Value = DBNull.Value; }
            else
            { ParamSave[1].Value = Convert.ToInt32(ProcessId); }

            ParamSave[2] = new SqlParameter("@PalletUID", SqlDbType.Decimal);
            if (Convert.ToString(PalletId).Trim().ToUpper() == "NULL")
            { ParamSave[2].Value = DBNull.Value; }
            else
            { ParamSave[2].Value = Convert.ToInt32(PalletId); }

            ParamSave[3] = new SqlParameter("@ItemId", SqlDbType.Decimal);
            if (Convert.ToString(ItemId).Trim().ToUpper() == "NULL")
            { ParamSave[3].Value = DBNull.Value; }
            else
            { ParamSave[3].Value = Convert.ToInt32(ItemId); }
            
            ParamSave[4] = new SqlParameter("@JulianCode", SqlDbType.VarChar, 20);
            if (Convert.ToString(JulianCode).Trim().ToUpper() == "NULL")
            { ParamSave[4].Value = DBNull.Value; }
            else
            { ParamSave[4].Value = Convert.ToString(JulianCode); }
            
            ParamSave[5] = new SqlParameter("@RejectionTypeGcd", SqlDbType.Decimal);
            if (Convert.ToString(RejectionType).Trim().ToUpper() == "NULL")
            { ParamSave[5].Value = DBNull.Value; }
            else
            { ParamSave[5].Value = Convert.ToInt32(RejectionType); }
            
            ParamSave[6] = new SqlParameter("@ReasonUIDs", SqlDbType.VarChar, 8000);                                   
            if (ReasonId.Trim() == "NULL")
            {  throw new Exception("Please select valid defect");  }
            else
            {  ParamSave[6].Value = ReasonId; }
            

            ParamSave[7] = new SqlParameter("@Message", SqlDbType.VarChar, 300);
            ParamSave[7].Direction = ParameterDirection.Output;
            ParamSave[8] = new SqlParameter("@ErrMessage", SqlDbType.VarChar, 300);
            ParamSave[8].Direction = ParameterDirection.Output;


            int rowaff = DataAccessHelper.DataAccess.ExecuteNonQuery(IConnection, "USP_QC_Mob_FTQ_Transaction_Insert", CommandType.StoredProcedure, ParamSave);

            if (!string.IsNullOrEmpty(Convert.ToString(ParamSave[8].Value).Trim()))
            {
               ErrMsg = Convert.ToString(ParamSave[8].Value).Trim();
            }

            DispMsg = Convert.ToString(ParamSave[7].Value).Trim();

            GetIceBerg_Det Lst = new GetIceBerg_Det();
            Lst.ErrorMsg = ErrMsg;
            Lst.DisplayMsg = DispMsg;
            LstIDs.Add(Lst);
            return LstIDs;

        }
    }
}
