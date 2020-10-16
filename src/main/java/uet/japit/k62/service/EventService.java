package uet.japit.k62.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uet.japit.k62.constant.AccountTypeConstant;
import uet.japit.k62.constant.MessageConstant;
import uet.japit.k62.constant.StatusCode;
import uet.japit.k62.dao.*;
import uet.japit.k62.exception.exception_define.common.UnAuthorException;
import uet.japit.k62.exception.exception_define.detail.CategoryHasExistedException;
import uet.japit.k62.exception.exception_define.detail.CategoryNotFoundException;
import uet.japit.k62.exception.exception_define.detail.EventNotFoundException;
import uet.japit.k62.models.entity.*;
import uet.japit.k62.models.request.ReqCreateCategory;
import uet.japit.k62.models.request.ReqCreateEvent;
import uet.japit.k62.models.request.ReqCreateLocation;
import uet.japit.k62.models.request.ReqCreateTicketClass;
import uet.japit.k62.models.response.data_response.ResEvent;
import uet.japit.k62.models.response.http_response.HttpResponse;
import uet.japit.k62.models.response.http_response.MessageResponse;
import uet.japit.k62.service.authorize.AttributeTokenService;
import uet.japit.k62.util.StringConvert;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    IUserDAO userDAO;

    @Autowired
    IEventDAO eventDAO;

    @Autowired
    ICategoryDAO categoryDAO;

    @Autowired
    DAO dao;

    public MessageResponse addEvent(HttpServletRequest httpRequest, ReqCreateEvent requestData) throws Exception {
        MessageResponse response = new MessageResponse();
        String token = httpRequest.getHeader("Authorization");
        String emailSendRequest = AttributeTokenService.getEmailFromToken(token);
        User userSendRequest = userDAO.findByEmail(emailSendRequest);
        Category category = categoryDAO.findById(requestData.getCategoryId()).get();
        if(category == null)
        {
            throw new CategoryNotFoundException();
        }
        Event newEvent = new Event(requestData);
        newEvent.setCategory(category);
        newEvent.setCreatedBy(userSendRequest.getEmail());
        eventDAO.save(newEvent);
        response.setStatusCode(StatusCode.OK);
        response.setMessage(MessageConstant.SUCCESS);
        return response;
    }

    public MessageResponse uploadImage(HttpServletRequest httpRequest,
                                       MultipartFile coverImage,
                                       MultipartFile mapImage,
                                       String eventId) throws Exception {
        String token = httpRequest.getHeader("Authorization");
        String emailSendRequest = AttributeTokenService.getEmailFromToken(token);
        User userSendRequest = userDAO.findByEmail(emailSendRequest);
        Event event = eventDAO.findById(eventId).get();
        if(event == null)
        {
            throw new EventNotFoundException();
        }
        if(emailSendRequest.equals(event.getCreatedBy()) ||
            userSendRequest.getAccountType().equals(AccountTypeConstant.ROOT)
        )
        {
            //upload CoverImage
            String currentPath = Paths.get("").toAbsolutePath().toString();
            String coverName = "cover_" + eventId + "_" +System.currentTimeMillis()+ "_" + StringConvert.convertStringToCode(coverImage.getOriginalFilename()).replaceAll("\\s","");;
            String saveToSeverPath = currentPath + "/src/main/resources/static/images/" + coverName;
            String savetoDBPath = "/static/images/" + coverName;
            coverImage.transferTo(new File(saveToSeverPath));
//        Files.copy(coverImage.getInputStream(), locationPath.resolve(coverName));
            event.setCoverImageUrl(savetoDBPath);

            //upload mapEvent Image
            String mapImageName = "map_" + eventId + "_" +System.currentTimeMillis()+ "_" + StringConvert.convertStringToCode(mapImage.getOriginalFilename());
            String saveMapToSeverPath = currentPath + "/src/main/resources/static/images/" + mapImageName;
            String saveMapToDBPath = "/static/images/" + mapImageName;
//        Files.copy(mapImage.getInputStream(), locationPath.resolve(mapImageName));
            mapImage.transferTo(new File(saveMapToSeverPath));
            event.setMapImageUrl(saveMapToDBPath);
            eventDAO.save(event);
            return new MessageResponse(StatusCode.OK, MessageConstant.SUCCESS);
        }
        else
        {
            throw new UnAuthorException();
        }
    }

    public HttpResponse getEvent(HttpServletRequest httpRequest)
    {
        return null;
    }

    public HttpResponse getEventById(String eventId) throws EventNotFoundException {
        Event event = eventDAO.findById(eventId).get();
        if(event == null)
        {
            throw new EventNotFoundException();
        }
        ResEvent resData = new ResEvent(event);
        return new HttpResponse(resData);
    }

    public HttpResponse search(String categoryId,
                               Integer time,
                               String location,
                               Boolean price)
    {
        HttpResponse response = new HttpResponse();
        List<Category> categoryList = categoryDAO.findAll();
        String query = "select * from event where inner join ticketclass" +
                " on event.id = ticketclass.event_id ";
        if(categoryId != null)
        {
            for(Category category : categoryList)
            {
                if (category.getId().equals(categoryId))
                {
                    query += "event.category_id=" + "'"  +categoryId + "'";
                }
            }
        }
        if(time != null && time instanceof Integer)
        {
//            query+=" and event.start_date between  " + new Date().getTime() + "and" + (new Date().getTime();

        }
        if(location != null)
        {
            if(location.equals("Hà Nội"))
            {
                query += " and event.city = \"Hà Nội\" ";
            }
            else  if(location.equals("Hồ Chí Minh"))
            {
                query += " and event.city = \"Hồ Chí Minh\" ";
            }
            else
            {
                query += " and event.city <> \"Hà Nội\" " +
                        " and event.city <>\"Hồ Chí Minh\"";
            }
        }
        if(price != null)
        {
            if(price == true)
            {
                query += " and ticketclass.price > 0";
            }
            else
            {
                query += "and ticketclass.price > 0";
            }
        }
        query += "groupBy event.id";
        System.out.println(query);
//        query += ";";
        List<Event> eventList = dao.search(query);
        response.setData(eventList);
        return response;
    }
}
