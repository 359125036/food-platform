##1.@RestControllerAdvice
````
     在spring3.2中，新增@RestControllerAdvice注解，用于定义
     @ExceptionHandler、@InitBinder、@ModelAttribute,并应用
     到所有@RequestMapping中。
     1.1 @InitBinder是应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     1.2 @ModelAttribute把值绑定到Model中，使全局@RequestMapping可以获取到该值
     1.3 @ExceptionHandler全局异常捕捉处理
````
###1.1 @InitBinder实例
````
    参考：https://blog.csdn.net/backbug/article/details/105317630
    @RestControllerAdvice
    public class initBinderDemo{
        /**
         * @Method initBinder
         * @Author zhengxin
         * @Description  在@RequestMapping中获取的时间类型参数，都转为Date类型，在@RequestMapping
         * 中直接用Date接收
          * @param binder
         * @Return void
         * @Exception 
         * @Date 2020/5/30 10:04
         */
        @InitBinder
            public void initBinder(WebDataBinder binder) {
                binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) {
                        Date date = null;
                        try {
                            if (text != null) {
                                date = DateUtils.parseDate(text, patterns);
                            }
                        } catch (ParseException e) {
                        }
                        setValue(date);
                    }
                });
            }
            private static String[] patterns = {
                    "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
                    "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
                    "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
            };
    }
````
###1.2 @ModelAttribute实例
````
   
    @RestControllerAdvice
    public class modelAttributeDemo{
        /**
         * @Method addAttributes
         * @Author zhengxin
         * @Description  把值绑定到Model中，使全局@RequestMapping可以获取到该值
          * @param model
         * @Return void
         * @Exception 
         * @Date 2020/5/30 10:13
         */
        @ModelAttribute
            public void addAttributes(Model model) {
                model.addAttribute("author", "Magical Sam");
            }
    }
    
    获取方式：
    @RequestMapping("/home")
    public String home(ModelMap modelMap) {
        System.out.println(modelMap.get("author"));
    }
    
    //或者 通过@ModelAttribute获取
    
    @RequestMapping("/home")
    public String home(@ModelAttribute("author") String author) {
        System.out.println(author);
    }
````
###1.3 @ExceptionHandler实例
````
    /**
     * @Method handlerMapUploadFile
     * @Author zhengxin
     * @Description 上传文件大于500k时，捕获异常：MaxUploadSizeExceededException
     * @param exception
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/30 9:39
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult handlerMapUploadFile(MaxUploadSizeExceededException exception){
        return JSONResult.errorMsg("文件上传大小不能超过500k,请压缩图片或降低图片质量！");
    }
````