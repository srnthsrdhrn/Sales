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

    // NOTE: If you change the class name "CrystalService" here, you must also update the reference to "CrystalService" in Web.config.
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class CrystalService : ICrystalService
    {
       
        #region Constructor

        public CrystalService()
        {
            //IntializeProperty();
        }

        #endregion

        #region Properties

        public string UserID { get; set; }
        public string Password { get; set; }
        private string ServiceType { get; set; }
        public DateTime FromDate { get; set; }
        public int ItemID { get; set; }
        public DataSet DataSetPR { get; set; }
        public DataSet DataSetEmp { get; set; }
        public DataSet DsNew {get;set;}
        public int serial{get;set;}
        public DataSet DsCon { get; set; }
        public string Src_ConStr { get; set; }
        public string Src_TableName { get; set; }
        public string Src_Fields { get; set; }
        public string Src_WhereCond { get; set; }
        public string Msg{get;set;}
        
        #endregion

        #region User Defined Functions

        private void IntializeProperty()
        {
            UserID = "";
            Password = "";
            ServiceType = "";
            FromDate = DateTime.Now;
            ItemID = 0;
            DataSetPR = null;
            DsCon = null;
            Src_ConStr = "";
            Src_TableName = "";
            Src_Fields = "";
            Src_WhereCond = "";
            
        }

        private void ValidateUserRights()
        {
            bool IsHavingRights = false;
            if (UserID == null || UserID == string.Empty)
            {
                throw new FaultException("Please Send UserID : in Set_UserID Property.");
            }
            else if (Password == null || Password == string.Empty)
            {
                throw new FaultException("Please Send Password : in Set_Password Property.");
            }
            else if (ServiceType == null || ServiceType == string.Empty)
            {
                throw new FaultException("Please Send ServiceType : in Set_ServiceType Property.");
            }
            try
            {
                object objRightsResult;
                SqlParameter[] Param = new SqlParameter[3];
                Param[0] = new SqlParameter("@UserID", SqlDbType.VarChar, 50);
                Param[0].Value = UserID;
                Param[1] = new SqlParameter("@Password", SqlDbType.VarChar, 50);
                Param[1].Value = Password;
                Param[2] = new SqlParameter("@ServiceType", SqlDbType.VarChar, 50);
                Param[2].Value = ServiceType;
                objRightsResult = DataAccessHelper.DataAccess.ExecuteScalar(DataAccessHelper.DataAccess.ConnectionString, "USP_WCF_CONFIGURATION_AUTHENTICATION", CommandType.StoredProcedure, Param);
                IsHavingRights = Convert.ToBoolean(objRightsResult);
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
        }

        private void ValidateInventoryStock()
        {
            if (ItemID == 0)
            {
                throw new FaultException("Please Send Item ID : in Set_ItemID Property.");
            }
        }

        private void ValidateSalesStock()
        {
            if (FromDate == null)
            {
                throw new FaultException("Please Send From Date : in Set_FromDate Property.");
            }
        }
        
        private void ValidatePurchaseRequisition()
        {
            if (DataSetPR == null)
            {
                throw new FaultException("Please Send DataSet for PR Generation : in Set_DataSetPR Property.");
            }
        }

        #endregion
                
        #region Crystal Services

        public DataSet GetInventoryStock()
        {
            DataSet dsInventory = new DataSet();
            ServiceType = "Inventory Stock";
            ValidateUserRights();
            ValidateInventoryStock();
            try
            {
                // Validate Item ID
                SqlParameter[] Param = new SqlParameter[3];
                Param[0] = new SqlParameter("@ItemID", SqlDbType.Decimal);
                Param[0].Value = ItemID;
                Param[1] = new SqlParameter("@Message", SqlDbType.VarChar, 500);
                Param[1].Direction = ParameterDirection.Output;
                Param[2] = new SqlParameter("@ErrorMessage", SqlDbType.VarChar, 500);
                Param[2].Direction = ParameterDirection.Output;
                dsInventory = DataAccessHelper.DataAccess.ExecuteDataSet(DataAccessHelper.DataAccess.ConnectionString, "USP_INVENTORY_STOCK", CommandType.StoredProcedure);
            }
            catch (FaultException ex)
            {
                dsInventory = null;
                throw new FaultException(ex.Message);
            }
            return dsInventory;
        }

        public DataSet GetSalesStock()
        {
            DataSet dsSalesStock = new DataSet();
            ServiceType = "Sales Stock";
            ValidateUserRights();
            ValidateSalesStock();
            try
            {
                //Validate From Date
                SqlParameter[] Param = new SqlParameter[3];
                Param[0] = new SqlParameter("@From_Dt", SqlDbType.DateTime);
                Param[0].Value = FromDate;
                Param[1] = new SqlParameter("@DisplayMessage", SqlDbType.VarChar, 500);
                Param[1].Direction = ParameterDirection.Output;
                Param[2] = new SqlParameter("@ErrorMessage", SqlDbType.VarChar, 500);
                Param[2].Direction = ParameterDirection.Output;
                dsSalesStock = DataAccessHelper.DataAccess.ExecuteDataSet(DataAccessHelper.DataAccess.ConnectionString, "USP_WCF_SALES_STOCK_FETCH", CommandType.StoredProcedure, Param);
                string DisplayMessage = "", ErrorMessage = "";
                DisplayMessage = Convert.ToString(Param[1].Value).Trim();
                ErrorMessage = Convert.ToString(Param[2].Value).Trim();
                if (!String.IsNullOrEmpty(ErrorMessage))
                {
                    if (!String.IsNullOrEmpty(DisplayMessage))
                    {
                        throw new FaultException(DisplayMessage);
                    }
                    else
                    {
                        throw new FaultException(ErrorMessage);
                    }
                }
            }
            catch (FaultException ex)
            {
                dsSalesStock = null;
                throw new FaultException(ex.Message);
            }
            return dsSalesStock;
        }

        public bool SetRaiseForPurchaseRequisition()
        {
            ServiceType = "Raise PR";
            ValidateUserRights();
            ValidatePurchaseRequisition();
            try
            {
                SqlParameter[] Param = new SqlParameter[5];
                Param[0] = new SqlParameter("@PRXML", SqlDbType.Xml);
                Param[0].Value = DataSetPR.GetXml();
                Param[1] = new SqlParameter("@ElementName", SqlDbType.VarChar, 100);
                Param[1].Value = "/" + DataSetPR.DataSetName + "/" + DataSetPR.Tables[0].TableName;
                Param[2] = new SqlParameter("@LoggedUser", SqlDbType.VarChar, 10);
                Param[2].Value = UserID;
                Param[3] = new SqlParameter("@DisplayMessage", SqlDbType.VarChar, 500);
                Param[3].Direction = ParameterDirection.Output;
                Param[4] = new SqlParameter("@ErrorMessage", SqlDbType.VarChar, 500);
                Param[4].Direction = ParameterDirection.Output;
                int RowsAffected = DataAccessHelper.DataAccess.ExecuteNonQuery(DataAccessHelper.DataAccess.ConnectionString, "USP_WCF_PR_SAVE_AND_GENERATION", CommandType.StoredProcedure, Param);
                string DisplayMessage = "", ErrorMessage = "";
                DisplayMessage = Convert.ToString(Param[3].Value).Trim();
                ErrorMessage = Convert.ToString(Param[4].Value).Trim();
                if (!String.IsNullOrEmpty(ErrorMessage))
                {
                    if (!String.IsNullOrEmpty(DisplayMessage))
                    {
                        throw new FaultException(DisplayMessage);
                    }
                    else
                    {
                        throw new FaultException(ErrorMessage);
                    }
                }
                return true;
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
            return false;
        }

        public DataSet GetPurchaseRequisitionStatus(string PurchaseRequisitionKey)
        {
            DataSet dsPR = new DataSet();
            try
            {
                SqlParameter[] Param = new SqlParameter[3];
                Param[0] = new SqlParameter("@WCFPRKey", SqlDbType.VarChar, 50);
                Param[0].Value = PurchaseRequisitionKey;
                Param[1] = new SqlParameter("@DisplayMessage", SqlDbType.VarChar, 500);
                Param[1].Direction = ParameterDirection.Output;
                Param[2] = new SqlParameter("@ErrorMessage", SqlDbType.VarChar, 500);
                Param[2].Direction = ParameterDirection.Output;
                dsPR = DataAccessHelper.DataAccess.ExecuteDataSet(DataAccessHelper.DataAccess.ConnectionString, "USP_WCF_PR_STATUS", CommandType.StoredProcedure, Param);
                string DisplayMessage = "", ErrorMessage = "";
                DisplayMessage = Convert.ToString(Param[1].Value).Trim();
                ErrorMessage = Convert.ToString(Param[2].Value).Trim();
                if (!String.IsNullOrEmpty(ErrorMessage))
                {
                    if (!String.IsNullOrEmpty(DisplayMessage))
                    {
                        throw new FaultException(DisplayMessage);
                    }
                    else
                    {
                        throw new FaultException(ErrorMessage);
                    }
                }
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
            return dsPR;
        }

        public bool SendSakthiSMS(string ProjectName, DataSet dsSMS, string UserName)
        {
            try
            {
                string SMSConnection = System.Configuration.ConfigurationManager.ConnectionStrings["SakthiSMS"].ConnectionString;

                int NoOfRecords = 0;
                if (dsSMS != null && dsSMS.Tables[0] != null)
                {
                    NoOfRecords = dsSMS.Tables[0].Rows.Count;
                }
                SqlParameter[] Param = new SqlParameter[6];
                Param[0] = new SqlParameter("@ProjectName", SqlDbType.VarChar, 50);
                Param[0].Value = ProjectName;
                Param[1] = new SqlParameter("@SMSxml", SqlDbType.Xml);
                Param[1].Value = dsSMS.GetXml();
                Param[2] = new SqlParameter("@ElementName", SqlDbType.VarChar, 50);
                Param[2].Value = "/" + dsSMS.DataSetName + "/" + dsSMS.Tables[0].TableName;
                Param[3] = new SqlParameter("@LoggedUser", SqlDbType.VarChar, 10);
                Param[3].Value = UserName;
                Param[4] = new SqlParameter("@DisplayMessage", SqlDbType.VarChar, 500);
                Param[4].Direction = ParameterDirection.Output;
                Param[5] = new SqlParameter("@ErrorMessage", SqlDbType.VarChar, 500);
                Param[5].Direction = ParameterDirection.Output;

                int RowsAffected = DataAccessHelper.DataAccess.ExecuteNonQuery(SMSConnection, "USP_WCF_SEND_SMS_SAVE", CommandType.StoredProcedure, Param);
                string DisplayMessage = "", ErrorMessage = "";
                DisplayMessage = Convert.ToString(Param[4].Value).Trim();
                ErrorMessage = Convert.ToString(Param[5].Value).Trim();
                if (!String.IsNullOrEmpty(ErrorMessage))
                {
                    if (!String.IsNullOrEmpty(DisplayMessage))
                    {
                        throw new FaultException(DisplayMessage);
                    }
                    else
                    {
                        throw new FaultException(ErrorMessage);
                    }
                }
            }
            catch (Exception ex)
            {
                throw new FaultException(ex.Message);
            }
            return true;
        }

        public DataSet GetConnectionDB(string ConName)
        {

            DataSet DsCon = new DataSet();
            SqlParameter[] SCPrm = new SqlParameter[1];
            SCPrm[0] = new SqlParameter("@ConnectionName", SqlDbType.VarChar, 20);
            SCPrm[0].Value = Convert.ToString(ConName);

            DsCon = DataAccessHelper.DataAccess.ExecuteDataSet(DataAccessHelper.DataAccess.ConnectionString, "USP_WCF_GetConnection_Details", CommandType.StoredProcedure, SCPrm);

            return DsCon;
        }

        public string DynamicDataInsertUpdate(string UserName, string Src_ConStr, string SDet, string DDet)
        {
            try
            {
                //Collection of Data
                string[] SrcDetstr = SDet.Split('#');//0 -Src tablename 1- Src fieldname 2- Src condition 3-Update Set Condition 4- Update Sel Col(Pky col),5- Src DBName

                DataSet DsOutTable = new DataSet();
                if (Convert.ToString(Src_ConStr).Trim() == "")
                { throw new FaultException("Connection Details not Available"); }
                else if (Convert.ToString(SrcDetstr[1]).Trim() == "")
                { throw new FaultException("Table Details not Available"); }

                    SqlParameter[] Spm = new SqlParameter[6];
                    Spm[0] = new SqlParameter("@LoggedUser", SqlDbType.VarChar, 10);
                    Spm[0].Value = UserName;
                    Spm[1] = new SqlParameter("@DisplayMessage", SqlDbType.VarChar, 500);
                    Spm[1].Direction = ParameterDirection.Output;
                    Spm[2] = new SqlParameter("@ErrorMessage", SqlDbType.VarChar, 500);
                    Spm[2].Direction = ParameterDirection.Output;
                    Spm[3] = new SqlParameter("@SourceTable", SqlDbType.VarChar);
                    Spm[3].Value =Convert.ToString(SrcDetstr[0]).Trim();
                    Spm[4] = new SqlParameter("@SourceFields", SqlDbType.VarChar);
                    Spm[4].Value =Convert.ToString(SrcDetstr[1]).Trim();
                    Spm[5] = new SqlParameter("@WhereClause", SqlDbType.VarChar);
                    Spm[5].Value =Convert.ToString(SrcDetstr[2]).Trim();


                    DsOutTable = DataAccessHelper.DataAccess.ExecuteDataSet(Src_ConStr, "USP_WCF_TableDetail_Collect", CommandType.StoredProcedure, Spm);//USP_WCF_HREmployeeInsertUpdate
                    string DisplayMessage = "", ErrorMessage = "";
                    DisplayMessage = Convert.ToString(Spm[1].Value).Trim();
                    ErrorMessage = Convert.ToString(Spm[2].Value).Trim();
                    if (!String.IsNullOrEmpty(ErrorMessage))
                    {
                        if (!String.IsNullOrEmpty(DisplayMessage))
                        {
                            throw new FaultException(DisplayMessage);
                        }
                        else
                        {
                            throw new FaultException(ErrorMessage);
                        }
                    }

                   //INSERT DATASET TO LOCAL DATABASE
                    if (DsOutTable != null || DsOutTable.Tables.Count > 0)
                    {
                        string[] DestDet = DDet.Split('#');

                        SqlParameter[] SqlPm = new SqlParameter[9];
                        SqlPm[0] = new SqlParameter("@XmlData", SqlDbType.Xml);
                        if (DsOutTable != null)
                        {
                            ClsGeneral ObjGen = new ClsGeneral();

                            ObjGen.RemoveEmptyRows(ref DsOutTable);
                            //Session["Table"] = dsSrc;
                            SqlPm[0].Value = DsOutTable.GetXml();
                        }

                        SqlPm[1] = new SqlParameter("@FromDB", SqlDbType.VarChar, 100);
                        SqlPm[1].Value = Convert.ToString(SrcDetstr[5]);  //source database

                        SqlPm[2] = new SqlParameter("@TableName", SqlDbType.VarChar, 100);
                        SqlPm[2].Value = Convert.ToString(DestDet[0]).Trim(); //dest Table

                        SqlPm[3] = new SqlParameter("@Remarks", SqlDbType.VarChar, 200);
                        SqlPm[3].Value = "Inserted";

                        SqlPm[4] = new SqlParameter("@CreatedBy", SqlDbType.VarChar, 7);
                        SqlPm[4].Value = UserName;

                        SqlPm[5] = new SqlParameter("@ErrorMessage", SqlDbType.VarChar, 5000);
                        SqlPm[5].Direction = ParameterDirection.Output;

                        SqlPm[6] = new SqlParameter("@ElemName", SqlDbType.VarChar, 200);
                        SqlPm[6].Value = "/" + Convert.ToString(DsOutTable.DataSetName).Trim() + "/" + Convert.ToString(DsOutTable.Tables[0].TableName).Trim();

                        SqlPm[7] = new SqlParameter("@DestFields", SqlDbType.VarChar);
                        SqlPm[7].Value = Convert.ToString(DestDet[1]).Trim();

                        SqlPm[8] = new SqlParameter("@DestWhereCond", SqlDbType.VarChar);
                        SqlPm[8].Value = Convert.ToString(DestDet[2]).Trim();

                        DataSet DsUpdate = DataAccessHelper.DataAccess.ExecuteDataSet(DataAccessHelper.DataAccess.ConnectionString, "Usp_WCF_CallCrystalWCF_Table_InsertUpdate", CommandType.StoredProcedure, SqlPm);

                        if (String.IsNullOrEmpty(Convert.ToString(SqlPm[5].Value)))
                        {
                       
                            string[] SrcDet = SDet.Split('#');
                            if (Convert.ToString(SrcDet[2]).Trim() == "")
                            { return "Source Where Condition Not Available"; }
                            if (DsUpdate != null && DsUpdate.Tables.Count != 0)
                            {
                                DataSet DsNew = new DataSet();
                                DsNew.Tables.Add(DsUpdate.Tables[2].Copy());
                                DsNew.Tables.Add(DsUpdate.Tables[3].Copy());
                                DsNew.AcceptChanges();

                                if (Convert.ToString(Src_ConStr) == "")
                                { throw new FaultException("Connection Could Not Be Established"); }
              
                                SqlParameter[] SUpm = new SqlParameter[8];
                                SUpm[0] = new SqlParameter("@XmlData", SqlDbType.Xml);
                                SUpm[0].Value = DsNew.GetXml(); 
                                SUpm[1] = new SqlParameter("@DisplayMessage", SqlDbType.VarChar, 500);
                                SUpm[1].Direction = ParameterDirection.Output;
                                SUpm[2] = new SqlParameter("@ErrorMessage", SqlDbType.VarChar, 500);
                                SUpm[2].Direction = ParameterDirection.Output;
                                SUpm[3] = new SqlParameter("@ElemName", SqlDbType.VarChar,500);
                                SUpm[3].Value = "/" + DsNew.DataSetName + "/" + DsNew.Tables[0].TableName;
                                SUpm[4] = new SqlParameter("@SrcTable",SqlDbType.VarChar);
                                SUpm[4].Value = Convert.ToString(SrcDetstr[0]).Trim();//SOURCE TABLE
                                SUpm[5] = new SqlParameter("@SetCond", SqlDbType.VarChar);
                                SUpm[5].Value = Convert.ToString(SrcDetstr[3]).Trim(); //3-Update Set Condition
                                SUpm[6] = new SqlParameter("@PkColName", SqlDbType.VarChar);
                                SUpm[6].Value = (Convert.ToString(SrcDetstr[4]).Trim() == "" ? "" : DsUpdate.Tables[4].Rows[0][0].ToString()); //  4- Update Sel Col(Pky col)
                                SUpm[7] = new SqlParameter("@SrcWhereCond", SqlDbType.VarChar);
                                SUpm[7].Value = (Convert.ToString(DestDet[2]).Trim());  //Dest where Condition


                                int rowsAffected = DataAccessHelper.DataAccess.ExecuteNonQuery(Src_ConStr, "USP_WCF_TableDetail_Update", CommandType.StoredProcedure, SUpm);
                                
                                DisplayMessage = Convert.ToString(Spm[1].Value).Trim();
                                ErrorMessage = Convert.ToString(Spm[2].Value).Trim();
                                if (!String.IsNullOrEmpty(ErrorMessage))
                                {
                                    if (!String.IsNullOrEmpty(DisplayMessage))
                                    {
                                        throw new FaultException(DisplayMessage);
                                    }
                                    else
                                    {
                                        throw new FaultException(ErrorMessage);
                                    }
                                }
                                else
                                {  Msg = "Updated Successfully"; }
                            }
                            else
                            {  throw new FaultException("Data Not Updated"); }
                        }
                        else
                        { throw new FaultException(SqlPm[5].Value.ToString()); }
                }
                return Msg;
            }
            catch (Exception Ex)
            {

                throw new FaultException(Ex.Message);
            }
        }

        #endregion

      
    }

}


public class MyException : Exception
{
    //
    // For guidelines regarding the creation of new exception types, see
    //    http://msdn.microsoft.com/library/default.asp?url=/library/en-us/cpgenref/html/cpconerrorraisinghandlingguidelines.asp
    // and
    //    http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dncscol/html/csharp07192001.asp
    //

    public MyException() { }
    public MyException(string message) : base(message) { }
    public MyException(string message, Exception inner) : base(message, inner) { }
    protected MyException(
      SerializationInfo info,
      StreamingContext context)
        : base(info, context) { }
}