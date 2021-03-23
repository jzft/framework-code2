package test.fetcher.utils;


import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class LinuxUtils {
	
	
	/**
     * 执行系统命令, 返回执行结果
     *
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static String execCmd(String[] cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);
            // 方法阻塞, 等待命令执行完成（成功会返回0）
           // System.out.println(process.waitFor());
            
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }

        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }
    
    public static String execCmd(String shell) throws IOException {  
		  int success = 0;  
		  StringBuffer sb = new StringBuffer();  
		  BufferedReader br = null;  		  
		  // get name representing the running Java virtual machine.  
//		  String name = ManagementFactory.getRuntimeMXBean().getName();  
//		  String pid = name.split("@")[0];  
		  Process process = null;
		  try {  
		    //System.out.println("Starting to exec{ " + shell + " }. PID is: " + pid);  
		     
		    ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", shell);  
		    pb.environment();  
		    pb.redirectErrorStream(true); // merge error stream into standard stream  
		    process = pb.start();  
		    if (process != null) {  
		      br = new BufferedReader(  
		          new InputStreamReader(process.getInputStream()), 1024);  
		      process.waitFor();  
		    } else {  
		      System.out.println("There is no PID found.");  
		    }  
		    sb.append("Ending exec right now, the result is：\n");  
		    String line = null;  
		    while (br != null && (line = br.readLine()) != null) {  
		      sb.append(line).append("\n");  
		    }  
		  } catch (Exception ioe) {  
		    sb.append("Error occured when exec cmd：\n").append(ioe.getMessage())  
		        .append("\n");  
		  } finally {  
			  if(process!=null){
				  process.destroy();
			  }
		    if (br != null) {  
		      br.close();  
		    }  
		  System.out.println(sb.toString());
		  System.out.println("execCmd finish!!");
		  }
		  return sb.toString();  
		}  
    
    public static void main(String[] args) throws IOException {
    	 PrintWriter writer = null;  
    	 StringBuffer sb = new StringBuffer();  
		  BufferedReader br = null;  		  
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
    	 sb.append("Ending exec right now, the result is：\n"); 
		    if (br != null) {  
		      br.close();  
		    }  
		    try {  
		      writer = new PrintWriter(System.out);  
		      writer.write(sb.toString());  
		    } catch (Exception e) {  
		     // LOG.error(e.getMessage(), e);  
		    } finally {  
		      writer.close();  
		    }  
	}
}