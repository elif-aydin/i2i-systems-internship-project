package i2icell.i2ibenimle.model;

/**
 * Created by elif on 25-Jul-17.
 */

public class Balance
{
    private int remainData;
    private int remainVoice;
    private int remainSMS;
    private String expirationDate;

    public int getRemainData()
    {
        return remainData;
    }

    public void setRemainData(int remainData)
    {
        this.remainData = remainData;
    }

    public int getRemainVoice()
    {
        return remainVoice;
    }

    public void setRemainVoice(int remainVoice)
    {
        this.remainVoice = remainVoice;
    }

    public int getRemainSMS()
    {
        return remainSMS;
    }

    public void setRemainSMS(int remainSMS)
    {
        this.remainSMS = remainSMS;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
