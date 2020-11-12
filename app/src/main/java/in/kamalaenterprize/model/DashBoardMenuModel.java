package in.kamalaenterprize.model;

public class DashBoardMenuModel {
	String menuName;
	int menuIcon;

	public DashBoardMenuModel() {}

	public DashBoardMenuModel(String menuName, int menuIcon) {
		super();
		this.menuName = menuName;
		this.menuIcon = menuIcon;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(int menuIcon) {
		this.menuIcon = menuIcon;
	}
}
