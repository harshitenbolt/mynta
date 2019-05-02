/**
 * @author admin Govinda A. Paliwal
 */
package com.canvascoders.opaper.Beans;
public class AadhaarCard 
{
    public String uid;
    public String name;
    public String gender;
    public String yob;
    public String co;
    public String house;
    public String lm;
    public String loc;
    public String vtc;
    public String po;
    public String dist;
    public String subdist;
    public String state;
    public String pincode;
    public String dob;
    public String originalXML;

    public String getFormattedUID() {

        return uid;
    }

    public String getAddress() {
        return house + ", " + lm + ", " + loc + ", " + dist + ", " + subdist + ", " + state + ".\nPincode: " + pincode;
    }
}
