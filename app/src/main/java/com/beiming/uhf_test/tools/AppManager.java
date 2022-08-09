package com.beiming.uhf_test.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;


import com.beiming.uhf_test.utils.LogPrintUtil;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理
 * @author duantianhui
 * @created 2017-2-27
 */
public class AppManager {

	private static Stack<Activity> activityStack = new Stack<Activity>();
	private static AppManager instance;

	private AppManager(){}
	/**
	 * 单一实例
	 */
	public static AppManager getAppManager(){
		if(instance == null){
			synchronized (AppManager.class) {
				if(instance == null) {
					instance = new AppManager();
				}
			}
		}
		return instance;
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		if (activity != null){
			LogPrintUtil.log("addActivity: " + activity.getClass().getSimpleName());
		}
		activityStack.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null && activityStack != null && activityStack.contains(activity)) {
			activityStack.remove(activity);
			if (!activity.isFinishing()) {
				activity.finish();
			}
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 判断Activity是否存在
	 */
	public boolean existsActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取MainActivity
	 */
//	public MainActivity getMainActivity() {
//		for (Activity activity : activityStack) {
//			if(activity instanceof MainActivity && !activity.isFinishing()){
//				return (MainActivity) activity;
//			}
//		}
//		return null;
//	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
			if (null != activityStack.get(i)){
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 关闭除了参数指定的界面之前的所有activity
	 * 使用场景：退出账号或被踢时，等登录界面启动完成后，再finish其他界面，体验上更好，避免用户看到黑屏
	 * @param cls
	 */
	public void finishAllExcept(Class cls) {
		if(activityStack == null || activityStack.size() <= 0) return;

//		printAllActivity();

		Stack<Activity> reserveStack = new Stack<>();

		for (Activity activity : activityStack) {
			if(activity.getClass().getName().equals(cls.getName())) { //参数指定的界面保留下来
				reserveStack.add(activity);
			}else {		//其余的finish掉
				activity.finish();
			}
		}

		activityStack.clear();
		activityStack.addAll(reserveStack);

//		printAllActivity();
	}

	/**
	 * 结束除了参数指定的activity以外的所有activity
	 * @param act
     */
	public void finishAllActExceptOne(Activity act) {
		for (int i = 0, size = activityStack.size(); i < size; i++){
			Activity activity = activityStack.get(i);
			if(activity != null && (activity != act)) {
				activity.finish();
			}
		}
		activityStack.clear();
		activityStack.add(act);
	}

	/**
	 * 打印当前所有activity
	 */
	public void printAllActivity() {
		if(activityStack == null) {
			LogPrintUtil.zhangshi("activityStack is empty");
			return;
		}
		StringBuffer sb = new StringBuffer();
		for(Activity activity : activityStack) {
			sb.append(activity.getClass().getSimpleName() + ", ");
		}
		LogPrintUtil.zhangshi(sb.toString());
	}

	public int activietyCounts(){
		if (null!=activityStack){
			return activityStack.size();
		}
		return 0;
	}
	/**
	 * 退出应用程序
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}

	/**
	 * 判断该页面是否是 在栈顶
	 * @param activity
	 * @return
	 */
	public boolean isTopActivity(Activity activity) {
		if(activity != null) {
			return activity == currentActivity();
		}else {
			return false;
		}
	}
	/**
	 * 获取顶部activity下面的第一个activity
	 * 使用场景：外部拉起app时，如果app已启动，则仍然先打开闪屏界面，此时需要直接打开指定的货源界面，startActivity的context需要是打开闪屏界面之前的那个activity
	 * @return
	 */
	public Activity getPreTopActivity(){
		if(activityStack.size() >= 2) {
			return activityStack.get(activityStack.size() - 2);
		}else {
			return null;
		}
	}

	/**
	 * 获取最顶部的 activity
	 * @return
	 */
	public Activity getTopActivity(){
		if(activityStack.size() >= 1) {
			return activityStack.get(activityStack.size() - 1);
		}else {
			return null;
		}
	}
	/**
	 * 获取最顶部的 第二个activity
	 * @return
	 */
	public Activity get2TopActivity(){
		if(activityStack.size() >= 2) {
			return activityStack.get(activityStack.size() - 2);
		}else {
			return null;
		}
	}

	/**
	 * 获取activity栈大小
	 * @return
	 */
	public int getActivitySize() {
		if(activityStack != null) {
			return activityStack.size();
		}else {
			return 0;
		}
	}

	/**
	 * 获取activity栈
	 */
	public Stack<Activity> getActivityStack() {
		return activityStack;
	}
}