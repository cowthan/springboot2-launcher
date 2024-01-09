export default {
  methods: {
    init: function (socket) {
      // 实例化socket
      this.socket = socket;
      // 监听socket连接
      this.socket.onopen = this.open;
      // 监听socket错误信息
      this.socket.onerror = this.error;
      // 监听socket消息
      this.socket.onmessage = this.onmessage;
      // 监听socket 关闭
      this.socket.onclose = this.close;
    },
    open: function () {
      console.log("连接成功");
    },
    error: function (e) {
      console.log("连接错误");
      console.dir(e);
    },
    onmessage: function (msg) {
      console.log("收到ws消息 - " + msg.data);
      const { IMUI } = this.$refs;
      let data = JSON.parse(msg.data);
      switch (data.event) {
        case "init":
          this.messageInitEvent(data, IMUI);
          break;
        case "friend_history_message":
          this.messageFriendHistoryEvent(data, IMUI);
          break;
        case "group_history_message":
          this.messageGroupHistoryEvent(data, IMUI);
          break;
        case "friend_withdraw_message":
          this.friendWithdrawMessageEvent(data, IMUI);
          break;
        case "friend_online_message":
          this.friendOnlineMessageEvent(data, IMUI);
          break;
        case "friend_offline_message":
          this.friendOfflineMessageEvent(data, IMUI);
          break;
        case "group_withdraw_message":
          this.groupWithdrawMessageEvent(data, IMUI);
          break;
        case "create_group":
          this.createGroupEvent(data, IMUI);
          break;
        case "edit_group":
          this.editGroupEvent(data, IMUI);
          break;
        case "new_member_join_group":
          this.newMemberJoinGroupEvent(data, IMUI);
          break;
        case "group_member_exit":
          this.groupMemberExitEvent(data, IMUI);
          break;
        case "delete_group_member":
          this.deleteGroupMemberEvent(data, IMUI);
          break;
        case "change_group_member_level":
          this.changeGroupMemberLevel(data, IMUI);
          break;
        case "delete_group":
          this.deleteGroup(data, IMUI);
          break;
        case "change_group_avatar":
          this.changeGroupAvatar(data, IMUI);
          break;
        case "new_friend_join_message":
          this.newFriendJoinMessage(data, IMUI);
          break;
        case "friend_delete_message":
          this.friendDeleteMessage(data, IMUI);
          break;
        default:
          this.getSendMessage(data, IMUI);
          break;
      }
    },
    send: function (message, uri, method = "GET") {
      let data = {
        message: message,
        uri: uri,
        method: method
      };
      this.socket.send(JSON.stringify(data));
      console.log("已send - " + message.id)
    },
    close: function () {
      console.log("连接关闭, 正在重连...");
      setTimeout(() => {
        // this.socket = new WebSocket(this.path + "&is_reconnection=true", [
        //   this.$store.getters.token
        // ]);
        //path: process.env.VUE_APP_WS_API,

        if (!this.path.startsWith('ws')) {
          this.path = 'ws://' + location.host + this.path;
        }
        this.socket = new WebSocket(this.path + "&is_reconnection=true");
        this.init(this.socket);
      }, 2000);
    }
  }
};
