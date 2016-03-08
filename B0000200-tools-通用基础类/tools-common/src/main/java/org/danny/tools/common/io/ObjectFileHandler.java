package org.danny.tools.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectFileHandler {

	/**
	 * 将指定的对象写入至指定的文件
	 * @param objs
	 * @param filename
	 */
	public static <T> void writeObjectsToFile(List<T> objs, String filename) {
		File file = new File(filename);

		try {
			ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream(file));

			for (Object obj : objs) {
				// 将对象写入文件
				objOutputStream.writeObject(obj);
			}

			// 关闭流
			objOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将指定文件中的对象数据读回
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	public static <T> List<T> readObjectsFromFile(String filename) throws FileNotFoundException {
		File file = new File(filename);

		// 如果文件不存在就抛出异常
		if (!file.exists())
			throw new FileNotFoundException();

		// 使用List先存储读回的对象
		List<T> list = new ArrayList<T>();

		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objInputStream = new ObjectInputStream(fileInputStream);

			while (fileInputStream.available() > 0) {
				list.add((T)objInputStream.readObject());
			}
			objInputStream.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将对象附加至指定的文件之后
	 * @param objs
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public static <T> void appendObjectsToFile(List<T> objs, String filename) throws FileNotFoundException {
		File file = new File(filename);

		// 如果文件不存在，则抛出异常
		if (!file.exists())
			throw new FileNotFoundException();

		try {
			// 附加模式
			ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream(file, true)) {
				/* 
				 * 注意，在试图将对象附加至一个先前已写入对象的文件时，
				 * 由于ObjectOutputStream在写入数据时，还会加上一个特别的流头(Stream Header)，所以在读取文件时会检查这个流头。
				 * 如果一个文件中被多次附加对象，那么该文件中会有多个流头，这样读取检查时就会发现不一致，这会丢出java.io.StreamCorruptedException异常。
				 * 为了解决这个问题，可以重新定义ObjectOutputStream的writeStreamHeader()方法，这样的话如果是以附加方式来写入对象，就不再重复写入流头
				 * @see java.io.ObjectOutputStream#writeStreamHeader()
				 */
				protected void writeStreamHeader() throws IOException {
				}
			};

			for (Object obj : objs) {
				// 将对象写入文件
				objOutputStream.writeObject(obj);
			}
			objOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		List<User> users = new ArrayList<User>();
		users.add(new User("cater", 101));
		users.add(new User("justin", 102));
		// 写入新文件
		writeObjectsToFile(users, args[0]);

		try {
			// 读取文件数据
			users = readObjectsFromFile(args[0]);
			// 显示读回的对象
			for (User user : users) {
				System.out.printf("%s\t%d%n", user.getName(), user.getNumber());
			}
			System.out.println();

			users = new ArrayList<User>();
			users.add(new User("momor", 103));
			users.add(new User("becky", 104));

			// 附加新对象至文件
			appendObjectsToFile(users, args[0]);

			// 读取文件数据
			users = readObjectsFromFile(args[0]);
			// 显示读回的对象
			for (User user : users) {
				System.out.printf("%s\t%d%n", user.getName(), user.getNumber());
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("没有指定文件名");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}

class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private int number;

	public User() {
	}

	public User(String name, int number) {
		this.name = name;
		this.number = number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}
}