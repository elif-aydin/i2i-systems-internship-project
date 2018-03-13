package i2icell.i2ibenimle.model;

/**
 * Created by elif on 24-Jul-17.
 */

public class Rateplan
{
    private int dataAmount;
    private String description;
    private String name;
    private int SMSAmount;
    private int voiceAmount;
    private int price;

    public int getDataAmount() {
        return dataAmount;
    }

    public void setDataAmount(int dataAmount) {
        this.dataAmount = dataAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSMSAmount() {
        return SMSAmount;
    }

    public void setSMSAmount(int SMSAmount) {
        this.SMSAmount = SMSAmount;
    }

    public int getVoiceAmount() {
        return voiceAmount;
    }

    public void setVoiceAmount(int voiceAmount) {
        this.voiceAmount = voiceAmount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
