<section class="chat">
<div id="body" class="message"> 
    
    <div id="chat-circle" class="btn btn-raised" style="overflow-y:hidden ;">
        <div id="chat-overlay"></div>
        <i class="fa-solid fa-paper-plane" style="color: #767F43;"></i>

        <!-- <i class="material-icons">speaker_phone</i> -->
    </div>
    
    <div class="chat-box ">
        <div class="chat-box-header">
        <a style="color: #111111;">Dikaosan Message</a>
        <span class="chat-box-toggle">
            <i class="fa-solid fa-xmark" style="color: #111111;"></i>
        </span>
        </div>
        <div class="chat-box-body">
        <div class="chat-box-overlay">   
        </div>
        <div class="chat-logs">
        </div><!--chat-log -->
        </div>
        <div class="chat-input">      
        <form>
            <input type="text" id="chat-input" placeholder="Send a message...">
        <button type="submit" class="chat-submit" id="chat-submit">
            <i class="fa-solid fa-paper-plane" style="color: #767F43;"></i>
        </button>
        </form>      
        </div>
    </div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-alpha.6/js/bootstrap.min.js"></script>
<script>
    $(function () {
    var INDEX = 0;
    $("#chat-submit").click(function (e) {
    e.preventDefault();
    var msg = $("#chat-input").val();
    if (msg.trim() == '') {
        return false;
    }
    generate_message(msg, 'self');
    var buttons = [
    {
        name: 'Existing User',
        value: 'existing' },

    {
        name: 'New User',
        value: 'new' }];


    setTimeout(function () {
        generate_message(msg, 'user');
    }, 1000);

    });

    function generate_message(msg, type) {
        INDEX++;
        var str = "";
        str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
        str += "          <span class=\"msg-avatar\">";
        str += "            <img src=\"https:\/\/image.crisp.im\/avatar\/operator\/196af8cc-f6ad-4ef7-afd1-c45d5231387c\/240\/?1483361727745\">";
        str += "          <\/span>";
        str += "          <div class=\"cm-msg-text\">";
        str += msg;
        str += "          <\/div>";
        str += "        <\/div>";
        $(".chat-logs").append(str);
        $("#cm-msg-" + INDEX).hide().fadeIn(300);
        if (type == 'self') {
        $("#chat-input").val('');
        }
        $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
    }

    function generate_button_message(msg, buttons) {
        /* Buttons should be object array 
        [
            {
            name: 'Existing User',
            value: 'existing'
            },
            {
            name: 'New User',
            value: 'new'
            }
        ]
        */
        INDEX++;
        var btn_obj = buttons.map(function (button) {
        return "              <li class=\"button\"><a href=\"javascript:;\" class=\"btn btn-primary chat-btn\" chat-value=\"" + button.value + "\">" + button.name + "<\/a><\/li>";
        }).join('');
        var str = "";
        str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg user\">";
        str += "          <span class=\"msg-avatar\">";
        str += "            <img src=\"https:\/\/image.crisp.im\/avatar\/operator\/196af8cc-f6ad-4ef7-afd1-c45d5231387c\/240\/?1483361727745\">";
        str += "          <\/span>";
        str += "          <div class=\"cm-msg-text\">";
        str += msg;
        str += "          <\/div>";
        str += "          <div class=\"cm-msg-button\">";
        str += "            <ul>";
        str += btn_obj;
        str += "            <\/ul>";
        str += "          <\/div>";
        str += "        <\/div>";
        $(".chat-logs").append(str);
        $("#cm-msg-" + INDEX).hide().fadeIn(300);
        $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
        $("#chat-input").attr("disabled", true);
    }

    $(document).delegate(".chat-btn", "click", function () {
        var value = $(this).attr("chat-value");
        var name = $(this).html();
        $("#chat-input").attr("disabled", false);
        generate_message(name, 'self');
    });

    $("#chat-circle").click(function () {
        $("#chat-circle").toggle('scale');
        $(".chat-box").toggle('scale');
    });

    $(".chat-box-toggle").click(function () {
        $("#chat-circle").toggle('scale');
        $(".chat-box").toggle('scale');
    });

    });
</script>
</section>