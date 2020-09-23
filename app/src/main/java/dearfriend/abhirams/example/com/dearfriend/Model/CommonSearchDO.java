package dearfriend.abhirams.example.com.dearfriend.Model;

public class CommonSearchDO {

    private String leftValue;
    private String rightValue;
    private String hiddenValue;

    public CommonSearchDO(String leftValue, String rightValue, String hiddenValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.hiddenValue = hiddenValue;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(String leftValue) {
        this.leftValue = leftValue;
    }

    public String getRightValue() {
        return rightValue;
    }

    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
    }

    public String getHiddenValue() {
        return hiddenValue;
    }

    public void setHiddenValue(String hiddenValue) {
        this.hiddenValue = hiddenValue;
    }
}
