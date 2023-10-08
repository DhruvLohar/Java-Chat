package testing;

public class StateManager {
    private static StateManager single_instance = null;

    public String username = "";
    public String title = "";
    public String desc = "";

    public String roomCode = "";
    public int maxNumOfUsers = 5;

    StateManager() {}

    public static synchronized StateManager getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new StateManager(); 
  
        return single_instance; 
    } 

    public String getUsername() {
      return this.username;
    }
    public void setUsername(String value) {
      this.username = value;
    }

    public String getTitle() {
      return this.title;
    }
    public void setTitle(String value) {
      this.title = value;
    }

    public String getDesc() {
      return this.desc;
    }
    public void setDesc(String value) {
      this.desc = value;
    }

    public String getRoomCode() {
      return this.roomCode;
    }
    public void setRoomCode(String value) {
      this.roomCode = value;
    }

    public int getMaxNumOfUsers() {
      return this.maxNumOfUsers;
    }
    public void setMaxNumOfUsers(int value) {
      this.maxNumOfUsers = value;
    }
}
