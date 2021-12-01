package net.javaguides.fileupload.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.mysql.cj.x.protobuf.MysqlxResultset.Row;

import net.javaguides.fileupload.dao.FileUploadDao;
import findway.*;

@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215) // upload file's size up to 16MB
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private FileUploadDao fileUploadDao;

	@Override
	public void init() {
		fileUploadDao = new FileUploadDao();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// gets values of text fields
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");

		InputStream inputStream = null; // input stream of the upload file

		// obtains the upload file part in this multipart request
		Part filePart = request.getPart("photo");
		if (filePart != null) {
			// prints out some information for debugging
			System.out.println(filePart.getName());
			System.out.println(filePart.getSize());
			System.out.println(filePart.getContentType());

			// obtains input stream of the upload file
			inputStream = filePart.getInputStream();
		}
		
		// Tim duong di
		int D=0;
		int count = 0;
		int i = 0;
		int data[][] = new int[6][6];
		Workbook wb = WorkbookFactory.create(inputStream);
		Sheet mySheet = wb.getSheetAt(0);
		for(org.apache.poi.ss.usermodel.Row row:mySheet)
		{
			count += 1;
			if(count == 1)
			{
				for(Cell cell:row)
				{
					D = (int)cell.getNumericCellValue();
					data = new int[D][D];
				}
			}
			else
			{
				int j = 0;
				for(Cell cell:row)
				{
					data[i][j] = (int)cell.getNumericCellValue();
					j++;
				}
				i++;
			}
		}
		
		StringBuffer buffer = new StringBuffer();
		TimDuongDi timduong = new TimDuongDi(data, D);
		int value[] = timduong.TimDuong();
		for(int k=0;k<value.length-1;k++)
		{
			buffer.append(value[k]);
			buffer.append("-");
		}
		buffer.append(value[value.length-1]);

		// sends the statement to the database server
		int row = fileUploadDao.uploadFile(firstName, lastName, buffer.toString());
		
		// sets the message in request scope
        request.setAttribute("Value", buffer);
         
        // forwards to the message page
        getServletContext().getRequestDispatcher("/message.jsp")
        .forward(request, response);
	}
}