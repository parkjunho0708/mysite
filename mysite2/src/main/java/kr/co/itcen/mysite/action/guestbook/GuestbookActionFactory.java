package kr.co.itcen.mysite.action.guestbook;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import kr.co.itcen.web.mvc.Action;
import kr.co.itcen.web.mvc.ActionFactory;

public class GuestbookActionFactory extends ActionFactory {

	@Override
	public Action getAction(String actionName) {
		Action action = null;
		Class<?> clazz = null;
		Constructor<?> constructor = null;
		GuestbookActionFactory guestbookActionFactory = new GuestbookActionFactory();
		
		actionName =
				guestbookActionFactory.getClass().getPackage().getName() + "."
                + actionName.substring(0, 1).toUpperCase() + actionName.substring(1)
                + "Action";

		try {
			clazz = Class.forName(actionName);
			constructor = clazz.getConstructor();
			action = (Action) constructor.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return action;
	}

}
