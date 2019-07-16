package ocrOnline;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ScanController {
    OCR o = new OCR();

    {
        try {
            o.loadList();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(42);
        }
    }

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello-world")
    @ResponseBody
    public Greeting sayHello(@RequestParam(name = "name", required = false, defaultValue = "Stranger") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    public final static String test = "iVBORw0KGgoAAAANSUhEUgAAAEoAAAAqCAYAAAAUJM0rAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsIAAA7CARUoSoAAAALYSURBVGhD7Zo/kpswFId/zlnEFjt7AnEC7N7tdqKEJl1Kd2lECZ1v4ApOACfIpIi4iyJAu7b4Y2kd72RH5pvRGHmAsb95TzxJbKQCK1a+6c8VC6soR/wW1baoshhhuMFmM2phiDir0OpTrXRjVI/gkqpu99X9GpOlvv2ZUrLZc90b5ULfawkhS0Znr502Kq23U3gYUS2yMMC2aHTfRoM0CJFZQss7UW32inTkiDKOUogue1QTECUHU+lzppMVo9K9WYbAckNwM5zZNK8cGKUe5SpR7sU0rZfTVEhOzXNZufxL/Iqo6oRCH/ZQjmNCdGcMQVKXKrIoKGUq4iTyaOlcz1Kv/fNLHw3Q/U7puEaEvK5R1zmuOOrxujx4ebL8+w/gtah74pUostvj8mFWHDL3gtKCXxFFdthfmmpSvNoKJEc8Sz31JDtyI6qaNEB4B1n+jVEkQS2msjahKij/wdf/F6XSIxhPWC3NGiG9rK5G0v2OpsA2UNfG2U3C/IuoN0hXI0kIzszoKtKbhPkrSkOSHLWa35Xm5E4L+8BSi57KOPH153oWxMLyC2XyyjSvx/uIMiAEUV5Dzo5f1wf7xxL1Rj9+jdNRyXpdLlAfU1TPEF2lGgfeUU/gnwuLUg8saiD6btZcxWne1MOLAnnCiz7sKU6zK52rKAR4NiuHWVZRjngkqkVbdXt4oaq6r24TjBD47bBh44moYYsq2BZomkaVRQfr9tM7k3X2Z5WMUzwRRbAzF6KQLj3nDZTgg6FpcZ3dm9QjyQ9clkQotn0KLgdWiyoe7wFS7Hfz6+wejVGq2jaqx25mskXQvWOg5iZnYd1YliGe2U2m/IjF3S0953PiUybFN7Rr7x6Iks1eY22WP+NRRA2QKO+3zB1KIw0F4wIyj3R/Hu9EdZAo6deghncM5pQNu8OclxCyRr6Yb2fWVxMd8TKiPoNVlCOrKEdWUY6sohxZRTkB/AX90R2a0E0xcQAAAABJRU5ErkJggg==";

    @GetMapping("/recognize")
    @ResponseBody
    public Result recognize(@RequestParam(name = "b64", required = false, defaultValue = test) String b64) {

        String start = "data:image/png;base64,";
        if (b64.startsWith(start)) {
            b64 = b64.substring(start.length());
        }

        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(b64);
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(decode));
            String cont = o.process(read, b64, false);
            return new Result(cont, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("", e.toString());
        }
    }

}

class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}