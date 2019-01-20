/**Country.java
 *
 * Description: The POJO for the database information.
 *
 * Date:11/24/2018
 * @author: Joseph Chang
 */

package edu.psu.bd.cs.jbc5740;

import java.util.ArrayList;

public class Country
{
    public String Name;
    public String Code;
    public String HeadOfState;
    public ArrayList<String> Languages;
    public ArrayList<String> Cities;

    public Country(String code, String name, String head)
    {
        Name = name;
        Code = code;
        HeadOfState = head;
    }
}
