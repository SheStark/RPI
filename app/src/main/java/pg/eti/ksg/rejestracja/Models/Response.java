package pg.eti.ksg.rejestracja.Models;

public class Response {
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(MessageCodes code) {
        this.code = code.getCode();
    }


    public Response(){}
    public Response(MessageCodes code)
    {
        this.code=code.getCode();
    }
}
