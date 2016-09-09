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
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Android" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Android.svc or Android.svc.cs at the Solution Explorer and start debugging.
    public class Android : IAndroid
    {
        #region Android
        //string AndroidConnection = System.Configuration.ConfigurationManager.ConnectionStrings["LineConn"].ConnectionString;

        string AndroidConnection = System.Configuration.ConfigurationManager.ConnectionStrings["CrysCon"].ConnectionString;

        public List<Machine_mst> GetMachine()
        {
            // string AndroidConnection = System.Configuration.ConfigurationManager.ConnectionStrings["LineConn"].ConnectionString;
            //string sql = "SELECT Uid As Id , MachineDescription AS Description FROM MachineM WHERE DelFlag = 'N' AND UnderMachineUid = 0";
            DataSet ds = new DataSet();

            ds = DataAccessHelper.DataAccess.ExecuteDataSet(AndroidConnection, "Usp_Android_Machine_Fetch", CommandType.StoredProcedure);

            List<Machine_mst> List = new List<Machine_mst>();

            foreach (DataRow dr in ds.Tables[0].Rows)
            {
                Machine_mst m = new Machine_mst();

                m.Id = Convert.ToInt32(dr["id"]);
                m.ParentId = Convert.ToInt32(dr["parentId"]);
                m.Description = Convert.ToString(dr["Description"]);
                m.Align = Convert.ToString(dr["Align"]);
                List.Add(m);
            }

            return List;
        }

        public List<User_mst> GetUser()
        {

            string sql = "select LTRIM(RTRIM(Userid)) AS UserId,Password , UserName,'' As PhoneNumber from userM WHERE DelFlag = 'N'";
            DataSet ds = new DataSet();

            ds = DataAccessHelper.DataAccess.ExecuteDataSet(AndroidConnection, sql, CommandType.Text);
            List<User_mst> l = new List<User_mst>();

            foreach (DataRow dr in ds.Tables[0].Rows)
            {
                User_mst u = new User_mst();

                u.UserId = Convert.ToString(dr["Userid"]);
                u.Password = Convert.ToString(dr["Password"]);
                u.UserName = Convert.ToString(dr["UserName"]);
                u.Phone = Convert.ToString(dr["PhoneNumber"]);

                l.Add(u);
            }

            return l;
        }

        public List<Schedule_Mst> GetSchedule()
        {
            string sql = "SELECT Uid , CONVERT(varchar,FromTime) AS FromTime,CONVERT(varchar,ToTime) AS ToTime FROM ScheduleDet WHERE ScheduleMUID =1 AND DelFlag = 'N'";
            DataSet ds = new DataSet();

            ds = DataAccessHelper.DataAccess.ExecuteDataSet(AndroidConnection, sql, CommandType.Text);
            List<Schedule_Mst> l = new List<Schedule_Mst>();

            foreach (DataRow dr in ds.Tables[0].Rows)
            {
                Schedule_Mst S = new Schedule_Mst();

                S.Id = Convert.ToInt32(dr["Uid"]);
                S.FromTime = Convert.ToString(dr["FromTime"]);
                S.ToTime = Convert.ToString(dr["ToTime"]);

                l.Add(S);
            }
            return l;
        }

        public List<Key_Value> GetProblem()
        {
            string sql = "SELECT UID,EvertDescription AS Description FROM EventM WHERE DelFlag = 'N' AND IsActive = 1";
            DataSet ds = new DataSet();

            ds = DataAccessHelper.DataAccess.ExecuteDataSet(AndroidConnection, sql, CommandType.Text);
            List<Key_Value> l = new List<Key_Value>();

            foreach (DataRow dr in ds.Tables[0].Rows)
            {
                Key_Value S = new Key_Value();

                S.Id = Convert.ToInt32(dr["Uid"]);
                S.Description = Convert.ToString(dr["Description"]);

                l.Add(S);
            }
            return l;
        }

        public int Save_Achivement(string UserId, string MachineId, string SchduleId, string ForDate, string LValue, string RValue, string FromTime, string ToTime, string ProblemId)
        {

            int i = 0;
            DateTime fordate = Convert.ToDateTime(ForDate);
            SqlParameter[] param = new SqlParameter[7];

            param[0] = new SqlParameter("@UserId", SqlDbType.VarChar);
            param[1] = new SqlParameter("@MachineId", SqlDbType.Decimal);
            param[2] = new SqlParameter("@ScheduleId", SqlDbType.Decimal);
            param[3] = new SqlParameter("@LValue", SqlDbType.Decimal);
            param[4] = new SqlParameter("@RValue", SqlDbType.Decimal);
            param[5] = new SqlParameter("@ForDate", SqlDbType.DateTime);
            param[6] = new SqlParameter("@Type", SqlDbType.Char, 1);

            param[0].Value = UserId;
            param[1].Value = Convert.ToDecimal(MachineId);
            param[2].Value = Convert.ToDecimal(SchduleId);
            param[3].Value = Convert.ToDecimal(LValue);
            param[4].Value = Convert.ToDecimal(RValue);
            param[5].Value = fordate;
            param[6].Value = "A";

            i = DataAccessHelper.DataAccess.ExecuteNonQuery(AndroidConnection, "Usp_Android_Save", CommandType.StoredProcedure, param);

            return i;
        }

        public int Save_Problem(string UserId, string MachineId, string SchduleId, string ForDate, string LValue, string RValue, string FromTime, string ToTime, string ProblemId)
        {
            //DateTime fordate = Convert.ToDateTime(save.ForDate);

            int i = 0;

            SqlParameter[] param = new SqlParameter[8];

            param[0] = new SqlParameter("@UserId", SqlDbType.VarChar);
            param[1] = new SqlParameter("@MachineId", SqlDbType.Decimal);
            param[2] = new SqlParameter("@ScheduleId", SqlDbType.Decimal);
            param[3] = new SqlParameter("@FromTIme", SqlDbType.Decimal);
            param[4] = new SqlParameter("@ToTime", SqlDbType.Decimal);
            param[5] = new SqlParameter("@ProblemId", SqlDbType.Decimal);
            param[6] = new SqlParameter("@ForDate", SqlDbType.DateTime);
            param[7] = new SqlParameter("@Type", SqlDbType.Char, 1);

            param[0].Value = UserId;
            param[1].Value = Convert.ToDecimal(MachineId);
            param[2].Value = Convert.ToDecimal(SchduleId);
            param[3].Value = Convert.ToDecimal(FromTime);
            param[4].Value = Convert.ToDecimal(ToTime);
            param[5].Value = Convert.ToDecimal(ProblemId);
            param[6].Value = Convert.ToDateTime(ForDate);
            param[7].Value = "P";

            i = DataAccessHelper.DataAccess.ExecuteNonQuery(AndroidConnection, "Usp_Android_Save", CommandType.StoredProcedure, param);

            return i;
        }

        public List<Chart_Report> GetScheduleReport()
        {
          
            DataSet ds = new DataSet();
            SqlParameter[] param = new SqlParameter[1];

            param[0] = new SqlParameter("@Type", SqlDbType.Char, 1);
            param[0].Value = "S";

            ds = DataAccessHelper.DataAccess.ExecuteDataSet(AndroidConnection, "Usp_Android_Chart_Reports", CommandType.StoredProcedure, param);

            List<Chart_Report> l = new List<Chart_Report>();

            foreach (DataRow dr in ds.Tables[0].Rows)
            {
                Chart_Report S = new Chart_Report();

                S.Target = Convert.ToInt32(dr["Target"]);
                S.Achieved = Convert.ToInt32(dr["Achieved"]);
                S.Description = Convert.ToString(dr["Description"]);

                l.Add(S);
            }
            return l;
        }

        public List<Chart_Report> GetLineReport()
        {

            DataSet ds = new DataSet();
            SqlParameter[] param = new SqlParameter[1];

            param[0] = new SqlParameter("@Type", SqlDbType.Char, 1);
            param[0].Value = "L";

            ds = DataAccessHelper.DataAccess.ExecuteDataSet(AndroidConnection, "Usp_Android_Chart_Reports", CommandType.StoredProcedure, param);

            List<Chart_Report> l = new List<Chart_Report>();

            foreach (DataRow dr in ds.Tables[0].Rows)
            {
                Chart_Report S = new Chart_Report();

                S.Target = Convert.ToInt32(dr["Target"]);
                S.Achieved = Convert.ToInt32(dr["Achieved"]);
                S.Description = Convert.ToString(dr["Description"]);

                l.Add(S);
            }
            return l;
        }

        public string Verify_BarCodeDetails(string SupplierCode, string Quantity, string Part, string DeliveryDocumentNo,string SerialNo)
        {
            string Msg;


            Msg = "Data Verified Successfully";
            return Msg;
        }

        #endregion
    }
}
