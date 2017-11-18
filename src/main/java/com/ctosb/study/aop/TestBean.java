package com.ctosb.study.aop;

//@Repository
public class TestBean implements TestB {
    private String username;

    private String password;

    public TestBean() {
        this.username = "alan";
        this.password = "123";
    }

    /* (non-Javadoc)
     * @see aop.TestB#getPassword()
     */
    @Override
    public String getPassword() {
        return "dd";
    }

    /* (non-Javadoc)
     * @see aop.TestB#setPassword(java.lang.String)
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /* (non-Javadoc)
     * @see aop.TestB#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

    /* (non-Javadoc)
     * @see aop.TestB#setUsername(java.lang.String)
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }
}
