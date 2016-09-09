using System;
using System.Data;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;
using System.Security;
using System.IdentityModel.Selectors;

namespace CrystalWCF
{
    public class MyCustomValidator : UserNamePasswordValidator
    {
        public override void Validate(string userName, string password)
        {
            // This isn't secure, though!
            if ((userName != "Nalan") || (password != "5"))
            {
                throw new Exception("Validation Failed!");
            }
        }
    }

}
