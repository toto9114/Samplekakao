/**
 * Copyright 2014 Daum Kakao Corp.
 *
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao.kakaotalk;

import com.kakao.auth.common.MessageSendable;
import com.kakao.kakaotalk.api.KakaoTalkApi;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.network.tasks.KakaoTaskQueue;
import com.kakao.friends.FriendContext;
import com.kakao.friends.api.FriendsApi;
import com.kakao.friends.response.FriendsResponse;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.ChatListResponse;
import com.kakao.kakaotalk.response.KakaoTalkProfile;

import java.util.Map;

/**
 * 카카오톡 API 요청을 담당한다.
 * @author leo.shin
 */
public class KakaoTalkService {

    /**
     * 가져올 채팅방의 타입.
     */
    public enum ChatType {
        /**
         * 1:1채팅방.
         */
        SINGLE("single"),
        /**
         * 그룹채팅방.
         */
        MULTI("multi"),
        /**
         * 1:1채팅방과 그룹채팅방 모두.
         */
        ALL("all");

        private final String name;

        ChatType(final String name) {
            this.name = name;
        }

        protected static ChatType getType(final String name){
            for(final ChatType type : ChatType.values()){
                if(type.name.equals(name))
                    return type;
            }
            return MULTI;
        }
    }

    /**
     * 카카오톡 프로필 요청
     * @param callback 요청 결과에 대한 callback
     */
    public static void requestProfile(final TalkResponseCallback<KakaoTalkProfile> callback) {
        requestProfile(callback, false);
    }

    /**
     * 카카오톡 프로필 요청
     * @param callback 요청 결과에 대한 callback
     * @param secureResource 이미지 url을 https로 반환할지 여부.
     */
    public static void requestProfile(final TalkResponseCallback<KakaoTalkProfile> callback, final boolean secureResource) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<KakaoTalkProfile>(callback) {
            @Override
            public KakaoTalkProfile call() throws Exception {
                return KakaoTalkApi.requestProfile(secureResource);
            }
        });
    }

    /**
     * 카카오톡 친구 리스트를 요청한다. Friends에 대한 접근권한이 있는 경우에만 얻어올 수 있다.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param callback 요청 결과에 대한 callback
     * @param context {@link FriendContext} 친구리스트 요청정보를 담고있는 context
     */
    public static void requestFriends(final TalkResponseCallback<FriendsResponse> callback, final FriendContext context) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<FriendsResponse>(callback) {
            @Override
            public FriendsResponse call() throws Exception {
                return FriendsApi.requestFriends(context);
            }
        });
    }

    /**
     * 카카오톡 메시지 전송하며, 리치메시지 3.5 spec으로 구성된 template으로 카카오톡 메시지 전송.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param callback 요청 결과에 대한 callback
     * @param receiverInfo 메세지 전송할 대상에 대한 정보를 가지고 있는 object
     * @param templateId 개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param args 메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg가 포함되어야 함.
     */
    public static void requestSendMessage(final TalkResponseCallback<Boolean> callback,
                                             final MessageSendable receiverInfo,
                                             final String templateId,
                                             final Map<String, String> args) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return KakaoTalkApi.requestSendMessage(receiverInfo, templateId, args);
            }
        });
    }

    /**
     * 톡의 채팅방 리스트 정보
     * 해당 유저의 group 챗방을 가져온다.
     * 기본 정렬은 asc로 최근 대화 순으로 정렬한다. (desc는 반대로 가장 오래된 대화 순으로 정렬한다.)
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param callback 요청 결과에 대한 callback
     * @param context {@link ChatListContext} 챗방리스트 요청정보를 담고있는 context
     */
    public static void requestChatList(final TalkResponseCallback<ChatListResponse> callback, final ChatListContext context) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<ChatListResponse>(callback) {
            @Override
            public ChatListResponse call() throws Exception {
                return KakaoTalkApi.requestChatList(context);
            }
        });
    }
}
