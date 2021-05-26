/*
 * Copyright (c) 2020 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef HI_DEMUXER_ERR_H
#define HI_DEMUXER_ERR_H

#ifdef __cplusplus
#if __cplusplus
extern "C" {
#endif
#endif /* __cplusplus */

/* general error code */
#define HI_ERR_DEMUXER_NULL_PTR       HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_ERR_NULL_PTR)
#define HI_ERR_DEMUXER_ILLEGAL_PARAM  HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_ERR_ILLEGAL_PARAM)
#define HI_ERR_DEMUXER_OPEN_FILE      HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_OPEN_FILE)
#define HI_ERR_DEMUXER_READ_PACKET    HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_READ_PACKET)
#define HI_ERR_DEMUXER_SEEK           HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_SEEK)
#define HI_ERR_DEMUXER_MEM_MALLOC     HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_MEM_MALLOC)
#define HI_ERR_DEMUXER_PROBE          HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_PROBE)
#define HI_ERR_DEMUXER_ACTION         HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_ACTION)
#define HI_ERR_DEMUXER_INVALID_HANDLE \
    HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_INVALID_HANDLE)
#define HI_ERR_DEMUXER_FREE_PACKET    HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_FREE_PACKET)
#define HI_ERR_DEMUXER_SET_ATTR       HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_SET_ATTR)
#define HI_ERR_DEMUXER_NOT_SUPPORT    HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_NOT_SUPPORT)
#define HI_ERR_DEMUXER_CLOSE_FILE     HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_CLOSE_FILE)
#define HI_ERR_DEMUXER_SKIP_FILE      HI_APP_DEF_ERR(HI_APPID_DEMUXER, APP_ERR_LEVEL_ERROR, APP_DEMUXER_ERR_SKIP)

#ifdef __cplusplus
#if __cplusplus
}
#endif
#endif /* __cplusplus */

#endif /* HI_DEMUXER_ERR_H */
