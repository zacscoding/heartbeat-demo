var existAgent = false;
var stompClient = null;
var requestCount = 0;

function getAgents() {
  console.log('getAgents is called..');
  $.ajax({
    url    : 'agents',
    success: function (agents) {
      console.log('> success to get agents\n', agents);
      var html = '';
      if (agents) {
        existAgent = true;
        for (var i = 0, size = agents.length; i < size; i++) {
          var agentName = agents[i].agentName;
          html += '<option value="' + agentName + '">' + agentName + '</option>';
        }
      } else {
        html = '<option>No Agents</option>';
      }

      var $target = $('#agent');
      $target.empty();
      $target.append(html);
    },
    error  : function (jqxhr) {
      console.log('> failed to get agents\n', jqxhr);
    }
  });
}

function requestAction(agentName, serviceName, actionType) {
  if (requestCount == 0) {
    $('.panel-collapse.in').collapse('hide');
  }
  requestCount++;

  $('#action-results').css('display', 'block');

  if (!isConnected()) {
    console.log('try to connect..');
    var socket = new SockJS('/heartbeat-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);
      sendAndSubscribe(agentName, serviceName, actionType);
    });
  } else {
    sendAndSubscribe(agentName, serviceName, actionType);
  }
}

function sendAndSubscribe(agentName, serviceName, actionType) {
  var requestId = new Date().getTime();
  stompClient.send('/app/action/' + agentName + '/' + serviceName + '/' + actionType + '/' + requestId, {}, JSON.stringify({}));

  var html = '<div class="panel-heading"><h4 class="panel-title">'
      + '<a data-toggle="collapse" href="#collapse-' + requestId + '">[#' + requestId + '-' + serviceName + '] >> [' + actionType + ']</a>'
      + '</h4></div><div id="collapse-' + requestId + '" class="panel-collapse collapse">'
      + '<textarea id="' + requestId + '-' + serviceName + '-' + actionType + '-logs" style="width: 100%;" rows="10"'
      + ' readonly="readonly"></textarea></div>';

  $('#action-results').append(html);

  var subscribe = stompClient.subscribe('/result/action/' + requestId + '/' + agentName + '/' + serviceName + '/' + actionType,
      function (message) {
        console.log('> receive message11 : ', message);
        var result = JSON.parse(message.body);
        var $textarea = $('#' + requestId + '-' + result.action.serviceName + '-' + result.action.actionType + '-logs');

        if (result.type == 'INPROGRESS') {
          $textarea.val($($textarea).val() + '\n' + result.log);
          if (!$('#collapse-' + requestId).hasClass('in')) {
            $('#collapse-' + requestId).collapse('toggle');
          }
        } else if (result.type == 'COMPLETE') {
          requestCount--;
          $textarea.val($($textarea).val() + '\n' + '>>>>>>>>>>    COMPLETE    <<<<<<<<<<\n\n');
          if (requestCount > 0) {
            $('.panel-collapse.in').collapse('hide');
          }
          subscribe.unsubscribe();
        }
        $textarea.scrollTop($textarea[0].scrollHeight);
      });
}

function isConnected() {
  console.log('check stompClient..', stompClient);
  if (typeof stompClient === 'undefined' || !stompClient) {
    return false;
  }

  return stompClient.connected;
}

$(function () {
  (function initialize() {
    getAgents();
  })();

  $('#btnAction').click(function () {
    var agentName = $('#agent option:selected').val();
    var serviceName = $('#serviceName').val();
    var actionType = $('#actionType option:selected').val();
    if (!serviceName) {
      alert('Service name is empty');
      $('#serviceName').focus();
      return;
    }

    requestAction(agentName, serviceName, actionType);
  });
});