//dwr.engine.setActiveReverseAjax(true);

var responseCodes = {
    '200': 'OK',
    '301': 'Moved Permanently',
    '302': 'Found',
    '304': 'Not Modified',
    '404': 'Not Found',
    '500': 'Internal Server Error',
    '201': 'Created',
    '202': 'Accepted',
    '203': 'Non-Authoritative Information',
    '204': 'No Content',
    '205': 'Reset Content',
    '206': 'Partial Content',
    '300': 'Multiple Choices',
    '303': 'See Other',
    '305': 'Use Proxy',
    '307': 'Temporary Redirect',
    '400': 'Bad Request',
    '401': 'Unauthorized',
    '402': 'Payment Required',
    '405': 'Method Not Allowed',
    '406': 'Not Acceptable',
    '407': 'Proxy Authentication Required',
    '408': 'Request Timeout',
    '410': 'Gone',
    '411': 'Length Required',
    '412': 'Precondition Failed',
    '413': 'Request Entity Too Large',
    '414': 'Request-URI Too Long',
    '415': 'Unsupported Media Type',
    '416': 'Requested Range Not Satisfiable',
    '417': 'Expectation Failed',
    '501': 'Not Implemented',
    '502': 'Bad Gateway',
    '503': 'Service Unavailable',
    '504': 'Gateway Timeout',
    '505': 'HTTP Version Not Supported'
};

function round(value, decimalPlaces) {
    return Math.round(value * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
}

function bytesLabelFunction(value) {
    if (value < 1024) {
        return value  + " B";
    }

    value = value / 1024;
    if (value < 1024) {
        return round(value, 0) + " KB";
    }

    value = value / 1024;

    if (value < 1024) {
        return round(value, 0) + " MB";
    }

    value = value / 1024;
    return round(value, 2) + " GB";
}

function responseTimeLabelFunction(value) {
    if (value < 1000) {
        return value  + " ms";
    }

    value = value / 1000;
    if (value < 10) {
        return round(value, 2) + " secs";
    }

    if (value < 60) {
        return round(value, 1) + " secs";
    }

    value = value / 60;
    return round(value, 1) + " min";
}

function showTooltip(x, y, contents) {
    $('<div id="tooltip" class="tooltip">' + contents + '</div>').css( {
        top: y + 5,
        left: x + 5
    }).appendTo("body").fadeIn(200);
}

function draw(e, blockStart, blockEnd, start, timeToFirstByte, timeToLastByte) {
    blockStart = Math.round(blockStart - (blockEnd - blockStart) * .01);
    blockEnd = Math.round(blockEnd + (blockEnd - blockStart) * .1);

    var txLen = blockEnd - blockStart;
    var ttfbRelStart = start - blockStart;

    var ttfbPixelStart = Math.floor(100 / (txLen / ttfbRelStart) * 4.5);
    var ttfbLen = Math.floor(100 / (txLen / timeToFirstByte) * 4.5);
    var ttlbPixelStart = ttfbPixelStart + ttfbLen;
    var ttlbLen = Math.floor(100 / (txLen / (timeToLastByte - timeToFirstByte)) * 4.5);

    if ((ttfbLen + ttlbLen) < 4) {
        ttfbLen = 2;
        ttlbLen = 2;

        ttlbPixelStart = ttfbPixelStart + ttfbLen;
    }

    var paper = new Raphael(e, 450, 20);

    var boundingBox = paper.rect(ttfbPixelStart, 5, ttfbLen + ttlbLen, 10)
            .attr("fill", "#FFF")
            .attr("stroke-width", "0");

    var ttfb = paper.rect(ttfbPixelStart, 5, ttfbLen, 10)
            .attr("fill", "#ccccff")
            .attr("stroke-width", "0");

    var ttlb = paper.rect(ttlbPixelStart, 5, ttlbLen, 10)
            .attr("fill", "#3333ff")
            .attr("stroke-width", "0");

    var respTimeLabel = responseTimeLabelFunction(timeToLastByte);
    var text = paper.text(ttlbPixelStart + ttlbLen + 17 + respTimeLabel.length, 10, respTimeLabel);

    var mouseover = function(evt) {
        ttlb.animate({scale: [1.0, 1.5, ttlbPixelStart, 0]}, 1000, "elastic");
        ttfb.animate({scale: [1.0, 1.5, ttlbPixelStart, 0]}, 1000, "elastic");
        boundingBox.animate({scale: [1.0, 1.5, ttlbPixelStart, 0]}, 1000, "elastic");
        boundingBox.attr("stroke-width", "2");

        var c = "<strong>Time to First Byte</strong>: " + timeToFirstByte + "ms<br/>";
        c += "<strong>Total time</strong>: " + timeToLastByte + "ms<br/>";

        showTooltip(evt.pageX,  evt.pageY, c);
    };

    var mouseout = function(evt) {
        ttlb.animate({scale: [1.0, 1.0, ttlbPixelStart, 0]}, 1000, "elastic");
        ttfb.animate({scale: [1.0, 1.0, ttlbPixelStart, 0]}, 1000, "elastic");
        boundingBox.animate({scale: [1.0, 1.0, ttlbPixelStart, 0]}, 1000, "elastic");
        boundingBox.attr("stroke-width", "0");
        $("#tooltip").remove();
    };

    ttfb.mouseover(mouseover).mouseout(mouseout);
    ttlb.mouseover(mouseover).mouseout(mouseout);
}

function displayBlock(block) {
    var div = $("#blockTemplate").clone().removeAttr('id').insertBefore("#blockTemplate").show().addClass("real");

    div.find(".objectCountHeader").html(block.objects.length + " objects");
    div.find(".bytesHeader").html(bytesLabelFunction(block.bytes));
    div.find(".responseTimeHeader").html(responseTimeLabelFunction(block.responseTime));

    for (var i = 0; i < block.objects.length; i++) {
        var obj = block.objects[i];
        var row = div.find("tbody tr:first").clone().show().appendTo(div.find("tbody"));
        row.find(".path .smallPath").html(obj.method + "&nbsp;" + obj.path);

        var fp = row.find(".path .fullPath");
        fp.html("<a href='" + obj.url + "' target='_blank'>" + obj.url + "</a>");


        row.find(".path").mouseover(function() {
            var offset = $(this).offset();

            var fullPath = $(this).find(".fullPath");
            fullPath.css({
                top: offset.top,
                left: offset.left
            });
            $(".fullPath").hide();
            fullPath.show();
        });

        fp.mouseout(function() {
            $(".fullPath").hide();
        });


        row.find(".responseCode div").html(obj.responseCode + "&nbsp;" + responseCodes[obj.responseCode]);
        if (obj.responseCode >= 400 || obj.responseCode < 200) {
            row.find(".responseCode").addClass("error_code");
        }

        row.find(".host div").html(obj.host);
        row.find(".bytes div").html(bytesLabelFunction(obj.bytes));

        var chartDiv = row.find(".chart div")[0];
        draw(chartDiv, block.start.getTime(), block.end.getTime(), obj.start.getTime(), obj.timeToFirstByte, obj.timeToLastByte);
    }
};

function sendBandwidth(value) {
    var current = $("#slider").slider('value');
    if (current == value) {
        ProxyServer.setBandwidth(value);
    }
};

function updateBandwidth(value, skipSending) {
    $("#bandwidthValue").html(value);

    if (!skipSending) {
        setTimeout(function() {
            sendBandwidth(value);
        }, 250);
    }
};

$(function() {
    $("#slider").slider({
        min: 1,
        max: 1000,
        slide: function(event, ui) {
            updateBandwidth(ui.value);
        }
    });

    $("#clearButton").click(function() {
        ProxyServer.clearBlocks(function() {
            $(".real").remove();
        });
    });

    var enabled = $("#enabled");
    var pattern = $("#pattern");
    var responseCode = $("#responseCode");
    var time = $("#time");
    var contentType = $("#contentType");
    var content = $("#content");

    for (var rc in responseCodes) {
        responseCode.append("<option value='" + rc + "'>" + rc + "&nbsp;" + responseCodes[rc] + "</option>");
    }

    ProxyServer.getBandwidth(function(value) {
        $("#slider").slider('value', value);
        updateBandwidth(value, true);
    });

    var setMockResponse = function() {
        ProxyServer.setMockResponse({
            enabled: enabled.attr('checked'),
            pattern: pattern.attr('value'),
            responseCode: responseCode.attr('value'),
            time: time.attr('value'),
            contentType: contentType.attr('value'),
            content: content.attr('value')
        });
    };

    ProxyServer.getMockResponse(function(mockResponse) {
        enabled.attr('checked', mockResponse.enabled);
        pattern.attr('value', mockResponse.pattern);
        responseCode.attr('value', mockResponse.responseCode);
        time.attr('value', mockResponse.time);
        contentType.attr('value', mockResponse.contentType);
        content.attr('value', mockResponse.content);

        $("#mockResponse input").change(setMockResponse);
        $("#mockResponse select").change(setMockResponse);
        $("#mockResponse textarea").change(setMockResponse);
    });

    ProxyServer.getBlocks(function(blocks) {
        for (var i = 0; i < blocks.length; i++) {
            displayBlock(blocks[i]);
        }
    });
});
