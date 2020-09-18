package pg.eti.ksg.rejestracja.Models;


public enum MessageCodes {
    INVALIDLOGIN(1),
    INVALIDEMAIL(2),
    INVALIDVALUES(3),
    ERROR(404),
    OK(200);

    private int code;

    public int getCode() {
        return code;
    }

    MessageCodes(int i) {
        this.code=i;
    }

    public static MessageCodes fromInt(int num)
    {
        for(MessageCodes m: MessageCodes.values())
        {
            if(m.getCode()==num)
                return m;
        }
        return MessageCodes.ERROR;
    }

}
