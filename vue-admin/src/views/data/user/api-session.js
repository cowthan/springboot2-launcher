import request from '@/utils/request'

// 查询会话列表
export function listSessions(query) {
    return request({
        url: '/admin/session/list',
        method: 'get',
        params: query
    })
}

// 强制下线
export function kickoff(ids) {
    return request({
        url: '/admin/session/kickoff?ids=' + ids,
        method: 'get'
    })
}