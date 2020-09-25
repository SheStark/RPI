package pg.eti.ksg.ProjektInzynierski.Models;

public class ResponseModel {
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(MessageCodes code) {
        this.code = code.getCode();
    }


    public ResponseModel(){}
    public ResponseModel(MessageCodes code)
    {
        this.code=code.getCode();
    }
}
