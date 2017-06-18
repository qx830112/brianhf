import com.qingxin.service.UserService;
import com.qingxin.user.bean.User;
import com.qingxin.user.exception.CreateConflictException;
import com.qingxin.user.exception.UserNotFoundException;

import junit.framework.TestCase;

public class UserControllerTest extends TestCase {

	
	
	public void testCreate(){
		User andy = new User();
		andy.setMailAddress("andy@example.com");
		User john = new User();
		john.setMailAddress("john@example.com");
		
		UserService service = UserService.getInstance();
		try {
			assertEquals(true,service.create(andy, john));
		} catch (UserNotFoundException | CreateConflictException e) {
			e.printStackTrace();
		}
	}

}
