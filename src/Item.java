import java.util.ArrayList;

/**
 * Created by wautel_l on 21/05/2017.
 */
public class Item {
    String name;
    int id;
    int number;
    String desc;
    String act1;
    String act2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item(String name, int id, int number, String desc, String action, String act2) {
        this.name = name;
        this.id = id;
        this.number = number;
        this.desc = desc;
        this.act1 = action;
        this.act2 = act2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String tmp)
    {
        desc = tmp;
    }

    public String getAction1()
    {
        return act1;
    }

    public String getAction2()
    {
        return act2;
    }


    public void setAction1(String tmp)
    {
        act1 = tmp;
    }

    public void setAct2(String tmp)
    {
        act2 = tmp;
    }

}

