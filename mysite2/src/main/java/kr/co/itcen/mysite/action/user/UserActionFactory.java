package kr.co.itcen.mysite.action.user;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import kr.co.itcen.web.mvc.Action;
import kr.co.itcen.web.mvc.ActionFactory;

public class UserActionFactory extends ActionFactory {

	@Override
	public Action getAction(String actionName) {
		//
		// actionName "JoinForm"
		// actionName + "Action" -> 객체를 생성하는 코드
		// String으로 된 클래스 이름으로 동적 객체 생성
		//
		Action action = null;
		Class<?> clazz = null;
		Constructor<?> constructor = null;
		UserActionFactory userActionFactory = new UserActionFactory();

		actionName =
                userActionFactory.getClass().getPackage().getName() + "."
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
