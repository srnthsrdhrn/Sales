using System;
using System.Data;
using System.Configuration;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;
using System.Data.SqlClient;

namespace CrystalWCF
{
    public class ClsGeneral
    {
        public void FillEmptyRows(ref DataSet dsClass)
        {
            DataRow drClass;
            if (dsClass != null)
            {
                for (int i = dsClass.Tables[0].Rows.Count; i < 10; i++)
                {
                    drClass = dsClass.Tables[0].NewRow();
                    dsClass.Tables[0].Rows.Add(drClass);
                }
                dsClass.AcceptChanges();
            }
        }

        /// <summary>
        /// Filling Empty Rows in DataSet with User Define Rows Count
        /// </summary>
        /// <param name="dsClass">A valid DataSet will Return With User Defined Rows.</param>
        /// <param name="NoOfRows">Number Of Rows Required.</param>
        public void FillEmptyRows(ref DataSet dsClass, int NoOfRows)
        {
            DataRow drClass;
            if (dsClass != null)
            {
                for (int i = dsClass.Tables[0].Rows.Count; i < NoOfRows; i++)
                {
                    drClass = dsClass.Tables[0].NewRow();
                    dsClass.Tables[0].Rows.Add(drClass);
                }
                dsClass.AcceptChanges();
            }
        }

        /// <summary>
        /// Filling Empty Rows in DataTable (Default - 10 Rows)
        /// </summary>
        /// <param name="dtClass">A valid DataTable will Return With Minimum 10 Rows.</param>
        public void FillEmptyRows(ref DataTable dtClass)
        {
            DataRow drClass;
            if (dtClass != null)
            {
                for (int i = dtClass.Rows.Count; i < 10; i++)
                {
                    drClass = dtClass.NewRow();
                    dtClass.Rows.Add(drClass);
                }
                dtClass.AcceptChanges();
            }
        }

        public DataSet FillEmptyRows(DataView dvClass)
        {
            DataSet dsClass = new DataSet();
            DataTable dtClass = new DataTable();
            DataRow drdvClass, drClass;
            dtClass = dvClass.Table.Clone();
            for (int i = 0; i < dvClass.Count; i++)
            {
                drdvClass = dvClass[i].Row;
                drClass = dtClass.NewRow();
                drClass.ItemArray = drdvClass.ItemArray;
                dtClass.Rows.Add(drClass);
                dtClass.AcceptChanges();
            }
            FillEmptyRows(ref dtClass);
            dsClass.Tables.Add(dtClass);
            return dsClass;
        }

        public DataSet FillEmptyRowsWithAlignSerialNumber(DataView dvClass, string ColumnName)
        {
            DataSet dsClass = new DataSet();
            DataTable dtClass = new DataTable();
            DataRow drdvClass, drClass;
            dtClass = dvClass.Table.Clone();
            for (int i = 0; i < dvClass.Count; i++)
            {
                drdvClass = dvClass[i].Row;
                drClass = dtClass.NewRow();
                drClass.ItemArray = drdvClass.ItemArray;
                dtClass.Rows.Add(drClass);
                dtClass.AcceptChanges();
            }
            AllignSerialNumber(ref dtClass, ColumnName);
            FillEmptyRows(ref dtClass);
            dsClass.Tables.Add(dtClass);
            return dsClass;
        }

        public DataSet FillEmptyRowsWithAlignSerialNumber(DataView dvClass, string ColumnName, int NoOfRows)
        {
            DataSet dsClass = new DataSet();
            DataTable dtClass = new DataTable();
            DataRow drdvClass, drClass;
            dtClass = dvClass.Table.Clone();
            for (int i = 0; i < dvClass.Count; i++)
            {
                drdvClass = dvClass[i].Row;
                drClass = dtClass.NewRow();
                drClass.ItemArray = drdvClass.ItemArray;
                dtClass.Rows.Add(drClass);
                dtClass.AcceptChanges();
            }
            AllignSerialNumber(ref dtClass, ColumnName);
            FillEmptyRows(ref dtClass, NoOfRows);
            dsClass.Tables.Add(dtClass);
            return dsClass;
        }

        /// <summary>
        /// Filling Empty Rows in DataTable with User Define Rows Count
        /// </summary>
        /// <param name="dtClass">A valid DataTable will Return With User Defined Rows.</param>
        /// <param name="NoOfRows">Number Of Rows Required.</param>
        public void FillEmptyRows(ref DataTable dtClass, int NoOfRows)
        {
            DataRow drClass;
            if (dtClass != null)
            {
                for (int i = dtClass.Rows.Count; i < NoOfRows; i++)
                {
                    drClass = dtClass.NewRow();
                    dtClass.Rows.Add(drClass);
                }
                dtClass.AcceptChanges();
            }
        }

        /// <summary>
        /// Delete Empty Rows in DataSet
        /// </summary>
        /// <param name="dsClass">A valid DataSet will Return With Out Blank Rows.</param>    
        public void RemoveEmptyRows(ref DataSet dsClass)
        {
            if (dsClass != null)
            {
                for (int i = 0; i < dsClass.Tables[0].Rows.Count; i++)
                {
                    if (Convert.ToString(dsClass.Tables[0].Rows[i].ItemArray[0]).Trim() == "" && Convert.ToString(dsClass.Tables[0].Rows[i].ItemArray[2]).Trim() == "")
                    {
                        dsClass.Tables[0].Rows[i].Delete();
                        i--;
                        dsClass.AcceptChanges();
                    }
                }
            }
        }

        /// <summary>
        /// Delete Empty Rows in DataSet With Checking Of Key Field
        /// </summary>
        /// <param name="dsClass">A valid DataSet will Return By Removing Empty Rows.</param>
        /// <param name="KeyField">Key Field To Be Check With DataSet.</param>
        public void RemoveEmptyRows(ref DataSet dsClass, string KeyFieldColumnName)
        {
            if (dsClass != null)
            {
                for (int i = 0; i < dsClass.Tables[0].Rows.Count; i++)
                {
                    if (Convert.ToString(dsClass.Tables[0].Rows[i][KeyFieldColumnName]).Trim() == "")
                    {
                        dsClass.Tables[0].Rows[i].Delete();
                        i--;
                        dsClass.AcceptChanges();
                    }
                }
            }
        }

        /// <summary>
        /// Delete Empty Rows in DataSet With Checking Of Key Field
        /// </summary>
        /// <param name="dsClass">A valid DataSet will Return By Removing Empty Rows.</param>
        /// <param name="KeyField">Key Field To Be Check With DataSet.</param>
        public void RemoveEmptyRows(ref DataSet dsClass, int KeyField)
        {
            if (dsClass != null)
            {
                for (int i = 0; i < dsClass.Tables[0].Rows.Count; i++)
                {
                    if (Convert.ToString(dsClass.Tables[0].Rows[i].ItemArray[KeyField]).Trim() == "")
                    {
                        dsClass.Tables[0].Rows[i].Delete();
                        i--;
                        dsClass.AcceptChanges();
                    }
                }
            }
        }

        /// <summary>
        /// Delete Empty Rows in DataTable With Checking Of Key Field
        /// </summary>
        /// <param name="dtClass">A valid DataTable will Return By Removing Empty Rows.</param>
        public void RemoveEmptyRows(ref DataTable dtClass)
        {
            if (dtClass != null)
            {
                for (int i = 0; i < dtClass.Rows.Count; i++)
                {
                    if (dtClass.Rows[i].ItemArray[0].ToString() == "" && dtClass.Rows[i].ItemArray[1].ToString() == "")
                    {
                        dtClass.Rows[i].Delete();
                        i--;
                        dtClass.AcceptChanges();
                    }
                }
            }




        }

        /// <summary>
        /// Delete Empty Rows in DataTable With Checking Of Key Field
        /// </summary>
        /// <param name="dtClass">A valid DataTable will Return By Removing Empty Rows.</param>
        /// <param name="KeyField">Key Field To Be Check With DataTable.</param>
        public void RemoveEmptyRows(ref DataTable dtClass, string KeyFieldColumnName)
        {
            if (dtClass != null)
            {
                for (int i = 0; i < dtClass.Rows.Count; i++)
                {
                    if (Convert.ToString(dtClass.Rows[i][KeyFieldColumnName]) == "")
                    {
                        dtClass.Rows[i].Delete();
                        i--;
                        dtClass.AcceptChanges();
                    }
                }
            }
        }

        /// <summary>
        /// Delete Empty Rows in DataTable With Checking Of Key Field
        /// </summary>
        /// <param name="dtClass">A valid DataTable will Return By Removing Empty Rows.</param>
        /// <param name="KeyField">Key Field To Be Check With DataTable.</param>
        public void RemoveEmptyRows(ref DataTable dtClass, int KeyField)
        {
            if (dtClass != null)
            {
                for (int i = 0; i < dtClass.Rows.Count; i++)
                {
                    if (Convert.ToString(dtClass.Rows[i].ItemArray[KeyField]) == "")
                    {
                        dtClass.Rows[i].Delete();
                        i--;
                        dtClass.AcceptChanges();
                    }
                }
            }
        }

        /// <summary>
        /// Function Will Return DataSet For The CommandText
        /// </summary>
        /// <param name="SPQuery">A Valid CommandText</param>
        /// <returns></returns>
        public DataSet FillDataSet(string SPQuery)
        {
            DataSet dsClass;
            dsClass = DataAccessHelper.DataAccess.ExecuteDataSet(DataAccessHelper.DataAccess.ConnectionString, SPQuery);
            return dsClass;
        }

        /// <summary>
        /// Get The Exact Row Position From The Navigation DataSet To Navigate
        /// </summary>
        /// <param name="dsClass">A valid Navigation DataSet.</param>
        /// <param name="KeyField">Return Primary Key Field Value.</param>
        /// <param name="RowPosition">Send Current RowPosition in Navigation DataSet.</param>
        /// <param name="ButtonPosition">Next Or Previous For Navigation.</param>
        public void FindRecordCount(DataSet dsClass, ref string KeyField, ref int RowPosition, string ButtonPosition)
        {
            DataRow drClass;
            if (dsClass != null)
            {
                if (ButtonPosition.ToUpper() == "NEXT")
                {
                    if (RowPosition < dsClass.Tables[0].Rows.Count - 1 && RowPosition >= 0)
                    {
                        RowPosition += 1;
                        drClass = dsClass.Tables[0].Rows[RowPosition];
                        KeyField = Convert.ToString(drClass[0]).Trim();
                        return;
                    }
                    else
                    {

                    }
                }
                else if (ButtonPosition.ToUpper() == "PREVIOUS")
                {
                    if (RowPosition < dsClass.Tables[0].Rows.Count && RowPosition > 0)
                    {
                        RowPosition -= 1;
                        drClass = dsClass.Tables[0].Rows[RowPosition];
                        KeyField = Convert.ToString(drClass[0]).Trim();
                        return;
                    }
                    else
                    {
                        RowPosition = 0;
                        drClass = dsClass.Tables[0].Rows[RowPosition];
                        KeyField = Convert.ToString(drClass[0]).Trim();
                        return;
                    }
                }
            }
        }

        /// <summary>
        /// Get The Exact Row Position From The Navigation DataSet
        /// </summary>
        /// <param name="dsClass">A valid Navigation DataSet.</param>
        /// <param name="KeyField">Primary Key Field Value To Match.</param>
        /// <param name="RowPosition">Return Current RowPosition in Navigation DataSet.</param>
        public void FindRecordCount(DataSet dsClass, ref string KeyField, ref int RowPosition)
        {
            DataRow drClass;
            if (dsClass != null)
            {
                for (int k = 0; k < dsClass.Tables[0].Rows.Count; k++)
                {
                    drClass = dsClass.Tables[0].Rows[k];
                    if (drClass[0].ToString().Trim() == KeyField.Trim())
                    {
                        RowPosition = k;
                        KeyField = Convert.ToString(drClass[0]).Trim();
                        break;
                    }
                }
            }
        }

        /// <summary>
        /// GetLogged Company Purchase Location Details For Purchase Requisition
        /// </summary>
        /// <param name="CompanyCode">Set Current Company Code.</param>
        /// <param name="LocationCode">Set Current Location Code.</param>
        /// <returns>Retur String Location Details.</returns>
        public string GetLoggedCompanyDetails(string CompanyCode, string LocationCode)
        {
            object objLocationName;
            SqlParameter[] ParamLoc = new SqlParameter[17];
            ParamLoc[0] = new SqlParameter("@Company", SqlDbType.Char, 3);
            ParamLoc[0].Value = CompanyCode;
            ParamLoc[1] = new SqlParameter("@Location", SqlDbType.Char, 3);
            ParamLoc[1].Value = LocationCode;
            objLocationName = DataAccessHelper.DataAccess.ExecuteScalar(DataAccessHelper.DataAccess.ConnectionString, "SP_PUR_PR_FETCH_LOCATION_DESCRIPTION", CommandType.StoredProcedure, ParamLoc);
            return Convert.ToString(objLocationName);
            //return Convert.ToString(Session["sessionLocation"]).Trim();
        }

        /// <summary>
        /// Get Row Position For Inserting New Row Into DataSet AND DataSet Should Have Minimum Three Columns
        /// </summary>
        /// <param name="dsClass">A valid DataSet For Getting Row to be Inserted.</param>
        /// <returns>returns System.int32 resulted by Row Position.</returns>
        public int InsertGridPos(DataSet dsClass)
        {
            DataRow datrw;
            int i = 0;
            for (i = 0; i < dsClass.Tables[0].Rows.Count; i++)
            {
                datrw = dsClass.Tables[0].Rows[i];
                if (Convert.ToString(datrw[0]).Trim() == "" && Convert.ToString(datrw[1]).Trim() == "" && Convert.ToString(datrw[2]).Trim() == "")
                {
                    return i;
                }
            }
            return i;
        }

        /// <summary>
        /// Get Row Position For Inserting New Row Into DataSet
        /// </summary>
        /// <param name="dsClass">A valid DataSet For Getting Row to be Inserted.</param>
        /// <param name="KeyField">Primary Key Field Name To Validate.</param>
        /// <returns>returns System.int32 resulted by Row Position.</returns>
        public int InsertGridPos(DataSet dsClass, int KeyField)
        {
            DataRow datrw;
            int i = 0;
            for (i = 0; i < dsClass.Tables[0].Rows.Count; i++)
            {
                datrw = dsClass.Tables[0].Rows[i];
                if (Convert.ToString(datrw[KeyField]).Trim() == "")
                {
                    return i;
                }
            }
            return i;
        }

        public void RemoveSession()
        {
            String strSesKeyName;
            for (int i = 0; i < System.Web.HttpContext.Current.Session.Count; i++)
            {
                strSesKeyName = System.Web.HttpContext.Current.Session.Keys[i];
                if (strSesKeyName == "sessioncompany" || strSesKeyName == "sessioncompanycode" || strSesKeyName == "sessionLocation" || strSesKeyName == "sessionLocationcode" || strSesKeyName == "sessionAccyear" || strSesKeyName == "sessionAccyearcode" || strSesKeyName == "LogeedUser" || strSesKeyName == "ISAdmin" || strSesKeyName == "Login_Key" || strSesKeyName == "btnState" || strSesKeyName == "SessionId" || strSesKeyName == "All_Flag" || strSesKeyName == "Rights" || strSesKeyName == "Favourites" || strSesKeyName == "AscOrder" || strSesKeyName == "id" || strSesKeyName == "DescOrder")
                {
                }
                else
                {
                    if (strSesKeyName.Length > 2)
                    {
                        if (strSesKeyName.Substring(0, 3) != "LOV")
                        {
                            System.Web.HttpContext.Current.Session.Remove(strSesKeyName);
                            i--;
                        }
                    }
                    else
                    {
                        System.Web.HttpContext.Current.Session.Remove(strSesKeyName);
                        i--;
                    }
                }
            }
        }

        public void GetTransactionKey(SqlTransaction Trans, ref string TransactionKey, ref decimal DocumentNo)
        {
            SqlParameter[] SqlParam = new SqlParameter[9];
            SqlParam[0] = new SqlParameter("@Key", SqlDbType.VarChar, 50);
            SqlParam[0].Direction = ParameterDirection.Output;
            SqlParam[1] = new SqlParameter("@StartNo", SqlDbType.Decimal);
            SqlParam[1].Direction = ParameterDirection.Output;
            SqlParam[2] = new SqlParameter("@CompanyCode", SqlDbType.Char, 3);
            SqlParam[2].Value = Convert.ToString(System.Web.HttpContext.Current.Session["sessioncompanycode"]).Trim();
            SqlParam[3] = new SqlParameter("@LocationCode", SqlDbType.Char, 3);
            SqlParam[3].Value = Convert.ToString(System.Web.HttpContext.Current.Session["sessionLocationcode"]).Trim();
            SqlParam[4] = new SqlParameter("@AccountYearCode", SqlDbType.Char, 3);
            SqlParam[4].Value = Convert.ToString(System.Web.HttpContext.Current.Session["sessionAccyearcode"]).Trim();
            SqlParam[5] = new SqlParameter("@VoucherType", SqlDbType.Char, 3);
            SqlParam[5].Value = Convert.ToString(System.Web.HttpContext.Current.Session["Doctype"]).Trim();   //Session["Doctype"].ToString().Trim());
            SqlParam[6] = new SqlParameter("@VoucherPrifix", SqlDbType.Char, 3);
            SqlParam[6].Value = Convert.ToString(System.Web.HttpContext.Current.Session["DocPrefix"]).Trim();
            SqlParam[7] = new SqlParameter("@LoggedUser", SqlDbType.VarChar, 7);
            SqlParam[7].Value = Convert.ToString(System.Web.HttpContext.Current.Session["LogeedUser"]).Trim();
            SqlParam[8] = new SqlParameter("@LastTransactionDate", SqlDbType.DateTime);
            SqlParam[8].Value = Convert.ToDateTime(System.Web.HttpContext.Current.Session["VDate"]);
            int RowsAffected = DataAccessHelper.DataAccess.ExecuteNonQuery(Trans, "SP_COMMON_DOCUMENT_NUMBER_FETCH", CommandType.StoredProcedure, SqlParam);
            TransactionKey = Convert.ToString(SqlParam[0].Value);
            DocumentNo = Convert.ToDecimal(SqlParam[1].Value);
        }

        public static string GetHomeCurrency()
        {

            string CompanyCode, LocationCode, HomeCurrency;
            CompanyCode = Convert.ToString(System.Web.HttpContext.Current.Session["sessioncompanycode"]);
            LocationCode = Convert.ToString(System.Web.HttpContext.Current.Session["sessionLocationcode"]);

            SqlParameter[] paramHomeCurrency = new SqlParameter[2];
            paramHomeCurrency[0] = new SqlParameter("@CompanyCode", CompanyCode);
            paramHomeCurrency[1] = new SqlParameter("@LocationCode", LocationCode);
            DataSet dsHomeCurrency = DataAccessHelper.DataAccess.ExecuteDataSet(DataAccessHelper.DataAccess.ConnectionString, "SP_Home_Currency_Fetch", CommandType.StoredProcedure, paramHomeCurrency);
            if (dsHomeCurrency != null)
            {
                if (dsHomeCurrency.Tables[0].Rows.Count > 0)
                {
                    HomeCurrency = Convert.ToString(dsHomeCurrency.Tables[0].Rows[0][0]);
                }
                else
                {
                    HomeCurrency = "";
                }
            }
            else
            {
                HomeCurrency = "";
            }
            return HomeCurrency;
        }

        public void AllignSerialNumber(ref DataSet dsAllign, string ColumnName)
        {
            DataRow drAllign;
            RemoveEmptyRows(ref dsAllign);
            for (int i = 0; i < dsAllign.Tables[0].Rows.Count; i++)
            {
                drAllign = dsAllign.Tables[0].Rows[i];
                drAllign.BeginEdit();
                drAllign[ColumnName] = i + 1;
                drAllign.EndEdit();
            }
            dsAllign.AcceptChanges();
        }

        public void AllignSerialNumber(ref DataSet dsAllign, int ColumnNo)
        {
            DataRow drAllign;
            RemoveEmptyRows(ref dsAllign);
            for (int i = 0; i < dsAllign.Tables[0].Rows.Count; i++)
            {
                drAllign = dsAllign.Tables[0].Rows[i];
                drAllign.BeginEdit();
                drAllign[ColumnNo] = i + 1;
                drAllign.EndEdit();
            }
            dsAllign.AcceptChanges();
        }

        public void AllignSerialNumber(ref DataTable dtAllign, string ColumnName)
        {
            DataRow drAllign;
            RemoveEmptyRows(ref dtAllign);
            for (int i = 0; i < dtAllign.Rows.Count; i++)
            {
                drAllign = dtAllign.Rows[i];
                drAllign.BeginEdit();
                drAllign[ColumnName] = i + 1;
                drAllign.EndEdit();
            }
            dtAllign.AcceptChanges();
        }

        public void AllignSerialNumber(ref DataTable dtAllign, int ColumnNo)
        {
            DataRow drAllign;
            RemoveEmptyRows(ref dtAllign);
            for (int i = 0; i < dtAllign.Rows.Count; i++)
            {
                drAllign = dtAllign.Rows[i];
                drAllign.BeginEdit();
                drAllign[ColumnNo] = i + 1;
                drAllign.EndEdit();
            }
            dtAllign.AcceptChanges();
        }

        public void GetHomeCurrency(ref string HomeCurrencyCode, ref string HomeCurrencyName, ref string HomeCurrencySymbol)
        {
            SqlDataReader rdr_customer;
            SqlParameter[] paramHomeCurrency = new SqlParameter[2];
            paramHomeCurrency[0] = new SqlParameter("@CompanyCode", SqlDbType.Char, 3);
            paramHomeCurrency[0].Value = Convert.ToString(System.Web.HttpContext.Current.Session["sessioncompanycode"]).Trim();
            paramHomeCurrency[1] = new SqlParameter("@LocationCode", SqlDbType.Char, 3);
            paramHomeCurrency[1].Value = Convert.ToString(System.Web.HttpContext.Current.Session["sessionLocationcode"]).Trim();
            rdr_customer = DataAccessHelper.DataAccess.ExecuteReader(DataAccessHelper.DataAccess.ConnectionString, "SP_Home_Currency_Fetch", CommandType.StoredProcedure, paramHomeCurrency);
            //WHILE READER READS  DATA FROM DATBASE
            while (rdr_customer.Read())
            {
                //IF THE ID FILED IN THE DATABASE IS NOT NULL
                if (!rdr_customer.IsDBNull(0))
                {
                    HomeCurrencyCode = Convert.ToString(rdr_customer["Currency_Cd"]).Trim();
                    HomeCurrencyName = Convert.ToString(rdr_customer["Currency_Name"]).Trim();
                    HomeCurrencySymbol = Convert.ToString(rdr_customer["Currency_sym"]).Trim();
                }
            }
            rdr_customer.Close();
        }

        public string GetBackGroundColor()
        {
            object objBackGroundColor;
            objBackGroundColor = DataAccessHelper.DataAccess.ExecuteScalar(DataAccessHelper.DataAccess.ConnectionString, "Select dbo.UFN_GET_BACKGROUND_COLOR('" + (string)System.Web.HttpContext.Current.Session["LogeedUser"] + "')", CommandType.Text);
            return (string)objBackGroundColor;
        }

        public static decimal GetForeignCurrencyFactor(string PurchaseOrderKey)
        {
            string CompanyCode, LocationCode, HomeCurrency;
            CompanyCode = Convert.ToString(System.Web.HttpContext.Current.Session["sessioncompanycode"]);
            LocationCode = Convert.ToString(System.Web.HttpContext.Current.Session["sessionLocationcode"]);

            SqlParameter[] paramForeignCurrencyFactor = new SqlParameter[3];
            paramForeignCurrencyFactor[0] = new SqlParameter("@CompanyCode", CompanyCode);
            paramForeignCurrencyFactor[1] = new SqlParameter("@LocationCode", LocationCode);
            paramForeignCurrencyFactor[2] = new SqlParameter("@POKey", PurchaseOrderKey);
            object objForeignCurrencyFactor = DataAccessHelper.DataAccess.ExecuteScalar(DataAccessHelper.DataAccess.ConnectionString, "Select DBO.UFN_GET_CURRENCY_CONVERSION_FACTOR(@COMPANYCODE,@LOCATIONCODE,@POKEY)", CommandType.Text, paramForeignCurrencyFactor);
            return Convert.ToDecimal(objForeignCurrencyFactor);
        }

        public static string GetPageURL()
        {
            string SessionId = "";
            SessionId = Convert.ToString(System.Web.HttpContext.Current.Session["SessionId"]).Trim();
            SqlParameter[] paramGetPageURL = new SqlParameter[1];
            paramGetPageURL[0] = new SqlParameter("@SessionId", SessionId);

            object objGetPageURL = DataAccessHelper.DataAccess.ExecuteScalar(DataAccessHelper.DataAccess.ConnectionString, "Select DBO.ufn_Common_Get_URL(@SessionId)", CommandType.Text, paramGetPageURL);
            return Convert.ToString(objGetPageURL);
        }

        public DataSet SwapDataTableIntoDataSet(DataTable dtClass)
        {
            DataSet dsClass = new DataSet();
            DataTable dtSwap = new DataTable();
            dtSwap = dtClass.Copy();
            dsClass.Tables.Add(dtSwap);
            return dsClass;
        }

    }
}