import java.sql.SQLException;
import java.util.ArrayList;

// My Class files
import Pojo.Users;


//$Id$

public class PojoChecker {
	Sql sql;

	public PojoChecker() {
		try {
			sql = Sql.getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new PojoChecker();
		Users users;
		
//		UsersBuilder userBuilder = new Users.UsersBuilder();

		ArrayList<Users> arrayList = new ArrayList<>();
		try {
			Sql.rs = Sql.smt.executeQuery("SELECT * FROM users LIMIT 0, 10");
			while (Sql.rs.next()) {
				users = new Users.UsersBuilder()
						.setId(Sql.rs.getInt("id"))
						.setUsername(Sql.rs.getString("username"))
						.setPassword(Sql.rs.getString("password"))
						.setPhone(Sql.rs.getString("phone"))
						.build();
				arrayList.add(users);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < arrayList.size(); i++) {
			System.out.println(arrayList.get(i));
		}
	}
}
