package i2icell.i2ibenimle.model;

/**
 * Created by elif on 24-Jul-17.
 */

public class Customer
{
    private String firstName;
    private int id;
    private String lastName;

    public String getFullName()
    {
        return firstName + " " + lastName;
    }
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}
