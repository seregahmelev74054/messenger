create index idx_members_conversation_id
    on members(conversation_id);

create index idx_members_user_id
    on members(user_id);

create index idx_message_deletions_message_id
    on message_deletions(message_id);