package kr.co.itcen.mysite.action.board;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class FiledownloadAction extends HttpServlet implements Action  {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String fileName = request.getParameter("filename"); //get으로 들어온 인자 받기
		
		String sDownloadPath = "D:\\itcen\\eclipse-workspace\\mysite2\\src\\main\\webapp\\assets\\images\\";
        String sFilePath = sDownloadPath + fileName;
        
        byte b[] = new byte[4096];
        FileInputStream fileInputStream = new FileInputStream(sFilePath);
        
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        String sMimeType = mimeTypesMap.getContentType(sFilePath);
        
		if (sMimeType == null) {
			sMimeType = "application/octet-stream";
		}
        
        response.setContentType(sMimeType);
        
        String sEncoding = new String(fileName.getBytes("utf-8"));
        response.setHeader("Content-Disposition", "attachment; filename= " + sEncoding);
        
        ServletOutputStream servletOutStream = response.getOutputStream();
        
        int numRead;
        while((numRead = fileInputStream.read(b,0,b.length))!= -1){
            servletOutStream.write(b,0,numRead);            
        }
        
        servletOutStream.flush();
        servletOutStream.close();
        fileInputStream.close();
		
		WebUtils.redirect(request, response, "/WEB-INF/views/board/view.jsp");
	}

}
